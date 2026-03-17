<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Машины</h2>
    <a href="${pageContext.request.contextPath}/admin/cars/create" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Добавить машину
    </a>
</div>

<c:if test="${param.message == 'created'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Машина успешно добавлена
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${param.message == 'updated'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Машина успешно обновлена
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${param.message == 'deleted'}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Машина удалена
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<c:choose>
    <c:when test="${empty cars}">
        <div class="alert alert-info">Машины не найдены</div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Марка</th>
                        <th>Модель</th>
                        <th>Год</th>
                        <th>Номер</th>
                        <th>Категория</th>
                        <th>Цвет</th>
                        <th>Статус</th>
                        <th>Цена/день</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="car" items="${cars}">
                        <tr>
                            <td>${car.id}</td>
                            <td>${car.brand}</td>
                            <td>${car.model}</td>
                            <td>${car.year}</td>
                            <td>${car.licensePlate}</td>
                            <td>${car.categoryName}</td>
                            <td>
                                <span style="display:inline-block;width:12px;height:12px;background:${car.colorHex};border:1px solid #ccc;border-radius:2px;vertical-align:middle;"></span>
                                ${car.colorName}
                            </td>
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
                            <td>${car.dailyCost} ₽</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/catalog/${car.id}"
                                   class="btn btn-sm btn-outline-info" title="Просмотр">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/cars/${car.id}/edit"
                                   class="btn btn-sm btn-outline-primary" title="Изменить">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/cars/${car.id}/delete"
                                      method="post" class="d-inline"
                                      onsubmit="return confirm('Удалить машину ${car.brand} ${car.model}?')">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Удалить">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary mt-3">
    <i class="bi bi-arrow-left"></i> Назад к панели
</a>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
