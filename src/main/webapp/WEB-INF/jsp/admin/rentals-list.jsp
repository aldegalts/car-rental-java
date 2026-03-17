<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Аренды — Админ</h2>

<div class="card shadow mb-4">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/rentals" method="get" class="row g-3 align-items-end">
            <div class="col-md-4">
                <label for="carId" class="form-label">Машина</label>
                <select class="form-select" id="carId" name="carId">
                    <option value="">Все</option>
                    <c:forEach var="car" items="${cars}">
                        <option value="${car.id}" ${car.id == selectedCarId ? 'selected' : ''}>
                            ${car.brand} ${car.model} (${car.licensePlate})
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label for="clientId" class="form-label">Клиент</label>
                <select class="form-select" id="clientId" name="clientId">
                    <option value="">Все</option>
                    <c:forEach var="client" items="${clients}">
                        <option value="${client.id}" ${client.id == selectedClientId ? 'selected' : ''}>
                            ${client.name} ${client.surname}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-primary">Фильтровать</button>
                <a href="${pageContext.request.contextPath}/admin/rentals" class="btn btn-outline-secondary">Сбросить</a>
            </div>
        </form>
    </div>
</div>

<c:choose>
    <c:when test="${empty rentals}">
        <div class="alert alert-info">Аренды не найдены.</div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Клиент</th>
                        <th>Машина</th>
                        <th>Начало</th>
                        <th>Окончание</th>
                        <th>Сумма</th>
                        <th>Статус</th>
                        <th>Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="rental" items="${rentals}">
                        <tr>
                            <td>${rental.id}</td>
                            <td>${rental.clientName}</td>
                            <td>${rental.carName}</td>
                            <td>${rental.startDate}</td>
                            <td>${rental.endDate}</td>
                            <td>${rental.totalAmount} &#8381;</td>
                            <td>
                                <c:choose>
                                    <c:when test="${rental.statusName == 'Активна'}">
                                        <span class="badge bg-success">${rental.statusName}</span>
                                    </c:when>
                                    <c:when test="${rental.statusName == 'Завершена'}">
                                        <span class="badge bg-secondary">${rental.statusName}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning">${rental.statusName}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="${pageContext.request.contextPath}/admin/rentals/${rental.id}"
                                       class="btn btn-outline-primary">Просмотр</a>
                                    <a href="${pageContext.request.contextPath}/admin/rentals/${rental.id}/edit"
                                       class="btn btn-outline-warning">Изменить</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
