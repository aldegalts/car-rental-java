<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Каталог автомобилей</h2>

<div class="row">
    <!-- Filter sidebar -->
    <div class="col-md-3">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <h5 class="card-title">Фильтры</h5>
                <form action="${pageContext.request.contextPath}/catalog" method="get">
                    <div class="mb-3">
                        <label for="brand" class="form-label">Марка</label>
                        <input type="text" class="form-control" id="brand" name="brand"
                               value="${filterBrand}" placeholder="Например, Toyota">
                    </div>

                    <div class="mb-3">
                        <label for="model" class="form-label">Модель</label>
                        <input type="text" class="form-control" id="model" name="model"
                               value="${filterModel}" placeholder="Например, Camry">
                    </div>

                    <div class="mb-3">
                        <label for="categoryId" class="form-label">Категория</label>
                        <select class="form-select" id="categoryId" name="categoryId">
                            <option value="">Все</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.id}" ${filterCategoryId == cat.id ? 'selected' : ''}>
                                    ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="colorId" class="form-label">Цвет</label>
                        <select class="form-select" id="colorId" name="colorId">
                            <option value="">Все</option>
                            <c:forEach var="color" items="${colors}">
                                <option value="${color.id}" ${filterColorId == color.id ? 'selected' : ''}>
                                    ${color.color}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Год выпуска</label>
                        <div class="row g-2">
                            <div class="col">
                                <input type="number" class="form-control" name="minYear"
                                       value="${filterMinYear}" placeholder="от">
                            </div>
                            <div class="col">
                                <input type="number" class="form-control" name="maxYear"
                                       value="${filterMaxYear}" placeholder="до">
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Цена в день (₽)</label>
                        <div class="row g-2">
                            <div class="col">
                                <input type="number" class="form-control" name="minCost"
                                       step="0.01" value="${filterMinCost}" placeholder="от">
                            </div>
                            <div class="col">
                                <input type="number" class="form-control" name="maxCost"
                                       step="0.01" value="${filterMaxCost}" placeholder="до">
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 mb-2">Найти</button>
                    <a href="${pageContext.request.contextPath}/catalog" class="btn btn-outline-secondary w-100">Сбросить</a>
                </form>
            </div>
        </div>
    </div>

    <!-- Car cards -->
    <div class="col-md-9">
        <c:choose>
            <c:when test="${empty cars}">
                <div class="alert alert-info">Автомобили не найдены</div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                    <c:forEach var="car" items="${cars}">
                        <div class="col">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body">
                                    <h5 class="card-title">${car.brand} ${car.model}</h5>
                                    <p class="card-text text-muted">${car.year} г.</p>
                                    <ul class="list-unstyled">
                                        <li><strong>Категория:</strong> ${car.categoryName}</li>
                                        <li>
                                            <strong>Цвет:</strong>
                                            <span style="display:inline-block;width:12px;height:12px;background:${car.colorHex};border:1px solid #ccc;border-radius:2px;vertical-align:middle;"></span>
                                            ${car.colorName}
                                        </li>
                                        <li><strong>Номер:</strong> ${car.licensePlate}</li>
                                        <li><strong>Статус:</strong>
                                            <c:choose>
                                                <c:when test="${car.statusName == 'Свободна'}">
                                                    <span class="badge bg-success">${car.statusName}</span>
                                                </c:when>
                                                <c:when test="${car.statusName == 'В аренде'}">
                                                    <span class="badge bg-warning text-dark">${car.statusName}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${car.statusName}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </ul>
                                    <p class="fs-5 fw-bold text-primary mb-2">${car.dailyCost} ₽/день</p>
                                </div>
                                <div class="card-footer bg-transparent">
                                    <a href="${pageContext.request.contextPath}/catalog/${car.id}"
                                       class="btn btn-outline-primary btn-sm">Подробнее</a>
                                    <c:if test="${not empty sessionScope.user && car.statusName == 'Свободна'}">
                                        <a href="${pageContext.request.contextPath}/rentals/create?carId=${car.id}"
                                           class="btn btn-primary btn-sm">Арендовать</a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
