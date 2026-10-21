<%@ tag pageEncoding="UTF-8" %>

<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<%@ attribute name="rating"
              required="true"
              type="java.lang.Integer" %>

<div class="rating">

    <c:forEach
        begin="1"
        end="${rating}">

        ⭐

    </c:forEach>

</div>