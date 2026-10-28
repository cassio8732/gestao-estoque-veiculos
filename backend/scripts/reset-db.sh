#!/usr/bin/env sh
# Remove o banco H2 em arquivo quando o schema antigo impede a subida (ex.: MARCA_ID em modelos).
set -eu
cd "$(dirname "$0")/.."
rm -f data/estoque.mv.db data/estoque.trace.db data/estoque_dev.mv.db data/estoque_dev.trace.db
echo "Banco H2 local removido. Execute: ./mvnw spring-boot:run"
