<%@ tag pageEncoding="UTF-8" %>

<%@ attribute name="product"
              required="true"
              type="com.shopsphere.model.Product" %>

<div class="product-card">

    <img src="${product.imageUrl}"
         alt="${product.name}"
         width="200">

    <h3>
        ${product.name}
    </h3>

    <p>
        Brand: ${product.brand}
    </p>

    <h3>
        ₹${product.price}
    </h3>

    <p>
        Stock: ${product.stock}
    </p>

    <a href="product-details?id=${product.productId}"
       class="btn">

        View Product

    </a>

</div>