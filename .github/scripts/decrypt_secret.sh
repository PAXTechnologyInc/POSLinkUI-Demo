#!/bin/sh

set -e

# 本脚本在 CI 中使用 GitHub Secrets 提供的 base64 编码 keystore
# 签名相关配置，生成 app/pax_demo.jks 和 app/signing.properties，

APP_DIR="./app"
mkdir -p "${APP_DIR}"

# 1. decode demo keystore（pax_demo.jks）
if [ -n "${PAX_DEMO_JKS_BASE64}" ]; then
  echo "${PAX_DEMO_JKS_BASE64}" | base64 -d > "${APP_DIR}/pax_demo.jks"
else
  echo "PAX_DEMO_JKS_BASE64 is not set, skip restoring pax_demo.jks"
fi

# 2. generate signing.properties
#    env：
#      PAX_DEMO_KEY_ALIAS
#      PAX_DEMO_KEY_PASSWORD
#      PAX_DEMO_STORE_PASSWORD

SIGNING_PROPS_FILE="${APP_DIR}/signing.properties"

cat > "${SIGNING_PROPS_FILE}" <<EOF
keyAlias=${PAX_DEMO_KEY_ALIAS}
keyPassword=${PAX_DEMO_KEY_PASSWORD}
storeFile=pax_demo.jks
storePassword=${PAX_DEMO_STORE_PASSWORD}
EOF

echo "signing.properties generated at ${SIGNING_PROPS_FILE}"

