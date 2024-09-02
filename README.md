# Суть проекта
Тут я учился/учусь Spring'у и бэкенду на языках JVM.
# Особенности проекта
- Все ручки и DTO генерируются через OpenApiGenerator задачей ```./gradlew openApiGenerate``` в директории [build/generated-sources/openapi-generated](build/generated-sources/openapi-generated) по файлу [openapi.yaml](openapi.yaml), а значит, в этом файле можно увидеть все ручки (описать их получше в планах)
- Так как я хотел в результате поулчше понимать SQL, для доступа к данным используется JDBC поверх PostreSQL вместо JPA
- Для авторизации и аутентификации используется формат JWT