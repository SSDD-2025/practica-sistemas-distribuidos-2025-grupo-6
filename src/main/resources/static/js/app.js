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