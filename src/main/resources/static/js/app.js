/**
 * Scrolls to the top of the page.
 */
function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

/**
 * Reloads the element with the given id with the content of the form with the given id.
 * @param {String} formId 
 * @param {String} elementId 
 */
function reloadWithForm(formId, elementId) {
    const form = document.getElementById(formId);
    // Fetch the form data and replace the content of the element with the response.
    fetch(form.action, {
        method: form.method,
        body: new FormData(form)
    })
        .then(res => res.text())
        .then(html => {
            document.getElementById(elementId).innerHTML = new DOMParser().parseFromString(html, 'text/html').getElementById(elementId).innerHTML;
        });

    scrollToTop();
}

/**
 * Confirms the removal of a product from the shopping cart.
 * @param {Long} productId 
 */
function confirmRemoval(productId) {
    if (confirm("Are you sure you want to remove this product from your shopping cart?")) {
        reloadWithForm('removeForm-' + productId, 'cart-list');
    }
}

/**
 * Confirms the action of a form with the given id.
 * @param {String} message 
 * @param {String} formId
 */
function confirmAction(message, formId) {
    if (confirm(message)) {
        document.getElementById(formId).submit();
    }
}


let page = 0;
const size = 8;

function reloadReviewsWithForm(userId) {
    $.ajax({
        url: `/api/users/${userId}/reviews`,
        method: "GET",
        data: {
            page: page,
            size: size
        },
        success: function (data) {
            const template = $("#review_template").html(); // ahora sí es un <script> con Mustache
            const container = $("#review_container");

            if (data.content.length === 0) {
                console.log("No hay más reviews para cargar.");
                return;
            }

            data.content.forEach(review => {
                const rendered = Mustache.render(template, review);
                container.append(rendered);
            });

            page++;
        },
        error: function (err) {
            console.error("Error:", err);
        }
    });

    scrollToTop();
}