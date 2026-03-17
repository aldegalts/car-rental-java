<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Аренда #${rental.id}</h2>

<div class="row">
    <div class="col-md-7">
        <div class="card shadow mb-4">
            <div class="card-header">
                <h5 class="mb-0">Информация об аренде</h5>
            </div>
            <div class="card-body">
                <table class="table table-borderless">
                    <tr>
                        <th>Клиент:</th>
                        <td>${rental.clientName}</td>
                    </tr>
                    <tr>
                        <th>Машина:</th>
                        <td>${rental.carName} (${rental.carLicensePlate})</td>
                    </tr>
                    <tr>
                        <th>Дата начала:</th>
                        <td>${rental.startDate}</td>
                    </tr>
                    <tr>
                        <th>Дата окончания:</th>
                        <td>${rental.endDate}</td>
                    </tr>
                    <tr>
                        <th>Сумма:</th>
                        <td><strong>${rental.totalAmount} &#8381;</strong></td>
                    </tr>
                    <tr>
                        <th>Статус:</th>
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
                    </tr>
                </table>

                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/admin/rentals/${rental.id}/edit"
                       class="btn btn-warning">Редактировать</a>
                    <form action="${pageContext.request.contextPath}/admin/rentals/${rental.id}/delete"
                          method="post" onsubmit="return confirm('Удалить аренду?');">
                        <button type="submit" class="btn btn-danger">Удалить</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/admin/rentals" class="btn btn-secondary">Назад к списку</a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="card shadow mb-4">
    <div class="card-header">
        <h5 class="mb-0">Нарушения</h5>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${empty violations}">
                <p class="text-muted mb-0">Нарушений нет.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Тип</th>
                                <th>Описание</th>
                                <th>Штраф</th>
                                <th>Дата</th>
                                <th>Оплата</th>
                                <th>Действия</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="v" items="${violations}">
                                <tr>
                                    <td>${v.id}</td>
                                    <td>${v.violationTypeName}</td>
                                    <td>${v.description}</td>
                                    <td>${v.fineAmount} &#8381;</td>
                                    <td>${v.violationDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${v.paid}">
                                                <span class="badge bg-success">Оплачено</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">Не оплачено</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="btn-group btn-group-sm">
                                            <a href="${pageContext.request.contextPath}/admin/rentals/${rental.id}/violations/${v.id}/edit"
                                               class="btn btn-outline-warning">Изменить</a>
                                            <form action="${pageContext.request.contextPath}/admin/rentals/${rental.id}/violations/${v.id}/delete"
                                                  method="post" class="d-inline"
                                                  onsubmit="return confirm('Удалить нарушение?');">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">Удалить</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="card shadow mb-4">
    <div class="card-header">
        <h5 class="mb-0">Зафиксировать нарушение</h5>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/rentals/${rental.id}/violations" method="post">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label for="violationTypeId" class="form-label">Тип нарушения *</label>
                    <select class="form-select" id="violationTypeId" name="violationTypeId" required
                            onchange="updateFine(this)">
                        <option value="">Выберите тип</option>
                        <c:forEach var="type" items="${violationTypes}">
                            <option value="${type.id}" data-fine="${type.defaultFine}">
                                ${type.typeName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="violationDate" class="form-label">Дата нарушения *</label>
                    <input type="datetime-local" class="form-control" id="violationDate" name="violationDate" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="fineAmount" class="form-label">Сумма штрафа *</label>
                    <input type="number" step="0.01" class="form-control" id="fineAmount" name="fineAmount" required>
                </div>
            </div>

            <div class="mb-3">
                <label for="description" class="form-label">Описание *</label>
                <textarea class="form-control" id="description" name="description" rows="2" required></textarea>
            </div>

            <div class="form-check mb-3">
                <input class="form-check-input" type="checkbox" id="paid" name="paid">
                <label class="form-check-label" for="paid">Оплачено</label>
            </div>

            <button type="submit" class="btn btn-primary">Зафиксировать</button>
        </form>
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
