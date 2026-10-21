
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#f7f5f1">
    <meta name="description" content="Discover thoughtful everyday essentials at ShopSphere.">
    <title>ShopSphere | Good finds, every day</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Style.css">
</head>
<body>
    <jsp:include page="/fragments/navbar.jsp" />

    <main>
        <section class="hero">
            <div class="hero-copy">
                <p class="eyebrow">A little better, every day</p>
                <h1>Good things.<br><span>Good feeling.</span></h1>
                <p class="hero-description">Thoughtful finds for your home, your routine and everywhere life takes you.</p>
                <a class="button button-dark" href="${pageContext.request.contextPath}/products">Explore the collection <span aria-hidden="true">→</span></a>
                <div class="hero-note"><span class="note-star" aria-hidden="true">✳</span> Curated for everyday living</div>
            </div>
            <div class="hero-art">
                <img src="${pageContext.request.contextPath}/images/hero-shopping.svg" alt="Illustration of a ShopSphere shopping bag with everyday essentials">
                <div class="hero-sticker">Find your<br>new favourite</div>
            </div>
        </section>

        <section class="benefit-strip" aria-label="ShopSphere benefits">
            <div><span aria-hidden="true">✦</span> Good quality, fair prices</div>
            <div><span aria-hidden="true">↗</span> Easy, secure checkout</div>
            <div><span aria-hidden="true">♡</span> Picks you’ll love</div>
        </section>

        <section class="section-wrap" id="collection">
            <div class="section-heading">
                <div>
                    <p class="eyebrow">Made for your everyday</p>
                    <h2>Little things, big joy.</h2>
                </div>
                <a class="text-link" href="${pageContext.request.contextPath}/products">Shop all products <span aria-hidden="true">→</span></a>
            </div>
            <div class="category-grid">
                <a class="category-card category-card-sand" href="${pageContext.request.contextPath}/products">
                    <img src="${pageContext.request.contextPath}/images/product1.jpg"
                         alt="Watch"
                         width="200"
                         height="200">
                </a>
                <a class="category-card category-card-rose" href="${pageContext.request.contextPath}/products">
                    <img src="${pageContext.request.contextPath}/images/product2.jpg"
                         alt="Bag"
                         width="200"
                         height="200">
                </a>
                <a class="category-card category-card-lilac" href="${pageContext.request.contextPath}/products">
                    <img src="${pageContext.request.contextPath}/images/product3.jpg"
                         alt="Headphone"
                         width="200"
                         height="200">
           
                </a>
                <a class="category-card category-card-peach" href="${pageContext.request.contextPath}/products">
                    <img src="${pageContext.request.contextPath}/images/product4.jpg"
                         alt="Shoes"
                         width="200"
                         height="200">
                 
                </a>
            </div>
        </section>

        <section class="bottom-cta">
            <div>
                <p class="eyebrow">Your next favourite is waiting</p>
                <h2>Find something<br>that just feels right.</h2>
            </div>
            <a class="button button-light" href="${pageContext.request.contextPath}/products">Shop the collection <span aria-hidden="true">→</span></a>
        </section>
    </main>

    <jsp:include page="/fragments/footer.jsp" />
</body>
</html>
