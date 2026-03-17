<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Редактирование нарушения #${violation.id}</h2>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/rentals/${rentalId}/violations/${violation.id}/edit"
                      method="post">
                    <div class="mb-3">
                        <label for="violationTypeId" class="form-label">Тип нарушения *</label>
                        <select class="form-select" id="violationTypeId" name="violationTypeId" required
                                onchange="updateFine(this)">
                            <c:forEach var="type" items="${violationTypes}">
                                <option value="${type.id}" data-fine="${type.defaultFine}"
                                        ${type.id == violation.violationTypeId ? 'selected' : ''}>
                                    ${type.typeName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="violationDate" class="form-label">Дата нарушения *</label>
                            <input type="datetime-local" class="form-control" id="violationDate" name="violationDate"
                                   value="${violation.violationDate}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="fineAmount" class="form-label">Сумма штрафа *</label>
                            <input type="number" step="0.01" class="form-control" id="fineAmount" name="fineAmount"
                                   value="${violation.fineAmount}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Описание *</label>
                        <textarea class="form-control" id="description" name="description"
                                  rows="3" required>${violation.description}</textarea>
                    </div>

                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" id="paid" name="paid"
                               ${violation.paid ? 'checked' : ''}>
                        <label class="form-check-label" for="paid">Оплачено</label>
                    </div>

                    <div class="d-flex gap-2">
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                        <a href="${pageContext.request.contextPath}/admin/rentals/${rentalId}"
                           class="btn btn-secondary">Отмена</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
function updateFine(select) {
    var option = select.options[select.selectedIndex];
    var fine = option.getAttribute('data-fine');
    if (fine) {
        document.getElementById('fineAmount').value = fine;
    }
}
</script>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
