export function createHTMLProduct(product) {
    // // const thumbnail = product.thumbnail || "/assets/template_image.png";
    // const searchQueryParam = encodeURIComponent(localStorage.getItem("originalSearchQuery") || searchQuery);
    return `
    <div class="card shadow-lg border-0 rounded-3 p-3">
        <div class="position-relative">
            <img src="/image/product/${product.id()}" class="card-img-top object-fit-cover rounded-3" style="height: 200px; width: 100%; object-fit: cover;">
            ${product.onSale}
            <span class="position-absolute top-0 start-0 bg-danger text-white px-2 py-1 rounded-3">-${product.sale()}%</span>
            ${/onSale}
        </div>
        <div class="card-body text-center">
            <h5 class="card-title fw-bold mb-1">${product.name()}</h5>
            <h6 class="card-subtitle mb-2 text-muted">${product.productType()}</h6>
            <div class="d-flex justify-content-center align-items-center gap-2 mb-2">
                <span class="fw-bold fs-5">${product.price()}</span>
            </div>
            <p class="text-success small mb-2">Stock: ${stock}</p>
            <a href="/product/${id}" class="btn btn-primary w-100">View Product</a>
        </div>
    </div>
    `;
}