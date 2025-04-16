const pageSize = 8;

let currentPage = 1;
let maxPage = 2;
let lastFormData = null;

// window.addEventListener("DOMContentLoaded", loadResults);

document.getElementById("filterForm").addEventListener("submit", function (e) {
  e.preventDefault();
  currentPage = 0;
	maxPage = 1;
  document.getElementById("productList").innerHTML = "";

  const formData = new FormData(this);
  lastFormData = new URLSearchParams(formData);

  loadResults();
});

document.getElementById("loadMore").addEventListener("click", loadResults);

function loadResults() {
	if (currentPage >= maxPage) {
		document.getElementById("loadMore").style.display = "none";
		return
	}
  document.getElementById("spinner").style.display = "block";

  const requestData = new URLSearchParams(lastFormData || "");
  requestData.append("page", currentPage++);
  requestData.append("size", pageSize);

  console.log(requestData.toString());

  Promise.all([
    fetch("/templates/product_preview_template.html").then((res) => res.text()),
    fetch(`/api/products/?${requestData.toString()}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    }).then((res) => res.json()),
  ])
    .then(([template, data]) => {
      console.log(template);
      let rendered = "";
      data["content"].forEach((item) => {
        rendered += Mustache.render(
          '<div class="col-3 p-3">' + template + "</div>",
          item
        );
      });

			maxPage = data["page"]["totalPages"];

			document
          .getElementById("productList")
          .insertAdjacentHTML("beforeend", rendered);
			if (currentPage < maxPage) {
      	document.getElementById("loadMore").style.display = "block";
			}
    })
    .finally(() => {
      document.getElementById("spinner").style.display = "none";
    });
}
