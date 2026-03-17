<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow">
            <div class="card-body">
                <h2 class="card-title mb-4">${car.brand} ${car.model}</h2>

                <table class="table">
                    <tbody>
                        <tr>
                            <th>Год выпуска</th>
                            <td>${car.year}</td>
                        </tr>
                        <tr>
                            <th>Гос. номер</th>
                            <td>${car.licensePlate}</td>
                        </tr>
                        <tr>
                            <th>Категория</th>
                            <td>${car.categoryName}</td>
                        </tr>
                        <tr>
                            <th>Цвет</th>
                            <td>
                                <span style="display:inline-block;width:14px;height:14px;background:${car.colorHex};border:1px solid #ccc;border-radius:2px;vertical-align:middle;"></span>
                                ${car.colorName}
                            </td>
                        </tr>
                        <tr>
                            <th>Статус</th>
                            <td>
                                <c:choose>
                                    <c:when test="${car.statusName == 'Доступен'}">
                                        <span class="badge bg-success">${car.statusName}</span>
                                    </c:when>
                                    <c:when test="${car.statusName == 'В аренде'}">
                                        <span class="badge bg-warning text-dark">${car.statusName}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${car.statusName}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>Стоимость аренды</th>
                            <td class="fs-5 fw-bold text-primary">${car.dailyCost} ₽/день</td>
                        </tr>
                    </tbody>
                </table>

                <div class="d-flex gap-2 mt-3">
                    <c:choose>
                        <c:when test="${not empty sessionScope.user && car.statusName == 'Доступен'}">
                            <a href="${pageContext.request.contextPath}/rentals/create/${car.id}"
                               class="btn btn-primary">Арендовать</a>
                        </c:when>
                        <c:when test="${empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                                Войдите, чтобы арендовать
                            </a>
                        </c:when>
                    </c:choose>
                    <a href="${pageContext.request.contextPath}/catalog" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> К каталогу
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
