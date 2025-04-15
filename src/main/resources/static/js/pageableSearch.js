const pageSize = 8;

let currentPage = 1;
let lastFormData = null;

document.getElementById('filterForm').addEventListener('submit', function (e) {
  e.preventDefault();
  currentPage = 1;
  document.getElementById('productList').innerHTML = '';

  const formData = new FormData(this);
  lastFormData = new URLSearchParams(formData);

  loadResults();
});

document.getElementById('loadMore').addEventListener('click', loadResults);

function loadResults() {
  document.getElementById('spinner').style.display = 'block';

  const requestData = new URLSearchParams(lastFormData || '');
  requestData.append('page', currentPage++);
  requestData.append('size', pageSize);

  Promise.all([
    fetch('templates/product_preview_template.html').then(res => res.text()),
    fetch(`/api/products/?${requestData.toString()}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      credentials: 'omit'
    }).then(res => res.json())
  ]).then(([template, data]) => {
    let rendered = '';
    data["content"].forEach(item => {
      rendered += Mustache.render(template, item);
    });
    document.getElementById('productList').insertAdjacentHTML('beforeend', rendered);
    document.getElementById('loadMore').style.display = 'block';
  }).finally(() => {
    document.getElementById('spinner').style.display = 'none';
  });
}
