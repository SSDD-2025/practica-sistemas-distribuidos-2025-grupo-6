const pageSize = 8;

let currentPage = 1;
let maxPage = 2;
let lastFormData = null;

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

  document.getElementById(this.loadMoreId).style.display = "none";

	if (currentPage >= maxPage) {
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
      credentials: "include"
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
    }).catch ((error) => {
      console.error("Error loading data:", error);
      document.getElementById(this.loadMoreId).style.display = "block";
    }).finally(() => {
      document.getElementById("spinner").style.display = "none";
    });
}
