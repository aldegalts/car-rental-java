<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">
    <c:choose>
        <c:when test="${not empty car.id}">Редактирование машины</c:when>
        <c:otherwise>Новая машина</c:otherwise>
    </c:choose>
</h2>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow">
            <div class="card-body">
                <c:set var="actionUrl" value="${pageContext.request.contextPath}/admin/cars/create" />
                <c:if test="${not empty car.id}">
                    <c:set var="actionUrl" value="${pageContext.request.contextPath}/admin/cars/${car.id}/edit" />
                </c:if>

                <form action="${actionUrl}" method="post">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="brand" class="form-label">Марка *</label>
                            <input type="text" class="form-control" id="brand" name="brand"
                                   value="${car.brand}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="model" class="form-label">Модель *</label>
                            <input type="text" class="form-control" id="model" name="model"
                                   value="${car.model}" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="year" class="form-label">Год выпуска *</label>
                            <input type="number" class="form-control" id="year" name="year"
                                   min="1900" max="2100" value="${car.year != 0 ? car.year : ''}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="licensePlate" class="form-label">Гос. номер *</label>
                            <input type="text" class="form-control" id="licensePlate" name="licensePlate"
                                   value="${car.licensePlate}" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="categoryId" class="form-label">Категория *</label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <option value="">Выберите категорию</option>
                                <c:forEach var="cat" items="${categories}">
                                    <option value="${cat.id}" ${car.categoryId == cat.id ? 'selected' : ''}>
                                        ${cat.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="colorId" class="form-label">Цвет *</label>
                            <select class="form-select" id="colorId" name="colorId" required>
                                <option value="">Выберите цвет</option>
                                <c:forEach var="color" items="${colors}">
                                    <option value="${color.id}" ${car.colorId == color.id ? 'selected' : ''}>
                                        ${color.color}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="dailyCost" class="form-label">Стоимость в день (₽) *</label>
                            <input type="number" class="form-control" id="dailyCost" name="dailyCost"
                                   step="0.01" min="0" value="${car.dailyCost}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="carStatusId" class="form-label">Статус *</label>
                            <select class="form-select" id="carStatusId" name="carStatusId" required>
                                <option value="">Выберите статус</option>
                                <c:forEach var="st" items="${statuses}">
                                    <option value="${st.id}" ${car.carStatusId == st.id ? 'selected' : ''}>
                                        ${st.status}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">
                            <c:choose>
                                <c:when test="${not empty car.id}">Сохранить</c:when>
                                <c:otherwise>Создать</c:otherwise>
                            </c:choose>
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/cars" class="btn btn-secondary">Отмена</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
