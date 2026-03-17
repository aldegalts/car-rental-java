<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

<h2 class="mb-4">Заполнение профиля</h2>

<div class="row justify-content-center">
    <div class="col-md-7">
        <div class="card shadow">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/profile/fill" method="post">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="name" class="form-label">Имя *</label>
                            <input type="text" class="form-control" id="name" name="name"
                                   value="${client.name}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="surname" class="form-label">Фамилия *</label>
                            <input type="text" class="form-control" id="surname" name="surname"
                                   value="${client.surname}" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="birthDate" class="form-label">Дата рождения *</label>
                            <input type="date" class="form-control" id="birthDate" name="birthDate"
                                   value="${client.birthDate}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="phone" class="form-label">Телефон *</label>
                            <input type="tel" class="form-control" id="phone" name="phone"
                                   value="${client.phone}" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email *</label>
                        <input type="email" class="form-control" id="email" name="email"
                               value="${client.email}" required>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="driverLicense" class="form-label">Номер ВУ *</label>
                            <input type="text" class="form-control" id="driverLicense" name="driverLicense"
                                   value="${client.driverLicense}" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="licenseExpiryDate" class="form-label">Срок действия ВУ *</label>
                            <input type="date" class="form-control" id="licenseExpiryDate" name="licenseExpiryDate"
                                   value="${client.licenseExpiryDate}" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary">Сохранить профиль</button>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
