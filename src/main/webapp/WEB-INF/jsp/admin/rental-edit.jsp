<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Редактирование аренды #${rental.id}</h2>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/rentals/${rental.id}/edit" method="post">
                    <div class="mb-3">
                        <label class="form-label">Клиент</label>
                        <input type="text" class="form-control" value="${rental.clientName}" disabled>
                    </div>

                    <div class="mb-3">
                        <label for="carId" class="form-label">Машина *</label>
                        <select class="form-select" id="carId" name="carId" required>
                            <c:forEach var="car" items="${cars}">
                                <option value="${car.id}" ${car.id == rental.carId ? 'selected' : ''}>
                                    ${car.brand} ${car.model} (${car.licensePlate})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="startDate" class="form-label">Дата начала *</label>
                            <input type="datetime-local" class="form-control" id="startDate" name="startDate"
                                   value="${rental.startDate}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="endDate" class="form-label">Дата окончания *</label>
                            <input type="datetime-local" class="form-control" id="endDate" name="endDate"
                                   value="${rental.endDate}" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="totalAmount" class="form-label">Сумма *</label>
                            <input type="number" step="0.01" class="form-control" id="totalAmount" name="totalAmount"
                                   value="${rental.totalAmount}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="rentalStatusId" class="form-label">Статус *</label>
                            <select class="form-select" id="rentalStatusId" name="rentalStatusId" required>
                                <c:forEach var="status" items="${statuses}">
                                    <option value="${status.id}" ${status.id == rental.rentalStatusId ? 'selected' : ''}>
                                        ${status.status}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                        <a href="${pageContext.request.contextPath}/admin/rentals/${rental.id}"
                           class="btn btn-secondary">Отмена</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
