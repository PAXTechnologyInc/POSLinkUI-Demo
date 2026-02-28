# -*- encoding:utf-8 -*-
"""
 ============================================================================
 COPYRIGHT
              Pax CORPORATION PROPRIETARY INFORMATION
   This software is supplied under the terms of a license agreement or
   nondisclosure agreement with Pax Corporation and may not be copied
   or disclosed except in accordance with the terms in that agreement.
      Copyright (C) 2020 - ? Pax Corporation. All rights reserved.
 Module Date: 2023/8/18  19:34
 Module Author: zhongch
 Description:
    Each whitelist is filled out in the form of a list. For example,
 to add the files R$attr.smali and R$drawable.smali to the code obfuscation whitelist,
 it would be: obfuscation_whitelist = ['R$attr.smali', 'R$drawable.smali'].
 ============================================================================
"""
# case 1：apk权限设置白名单
# 示例：permissions_whitelist = [’android.permission.READ_EXTERNAL_STORAGE‘]
permissions_whitelist = ["com.pax.edc.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION",
                         "android.permission.READ_EXTERNAL_STORAGE",
                         "android.permission.WRITE_EXTERNAL_STORAGE",
                         "android.permission.CHANGE_CONFIGURATION",
                         "android.permission.CAMERA",
                         "android.permission.READ_PHONE_STATE",
                         "com.market.android.app.OPEN_DETAIL_PAGE",
                         "com.market.android.app.OPEN_DOWNLOAD_LIST",
                         "com.market.android.app.ONLINE_LOCATION",
                         "com.market.android.app.SN_HOST",
                         "com.pax.permission.MAGCARD",
                         "com.pax.permission.PED",
                         "com.pax.permission.ICC",
                         "com.pax.permission.PICC",
                         "com.pax.permission.PRINTER"]

# case 3：代码混淆白名单
# 示例：obfuscation_whitelist = ['R$attr.smali']
obfuscation_whitelist = ['R$attr.smali', 'R$drawable.smali', 'R$id.smali', 'R$layout.smali', 'R$styleable.smali',
                         'IOCR$IOCRListener.smali', 'ILPR$ILPRListener.smali']

# case 8：IP地址白名单
# 示例：ip_address_whitelist = [{'com.pax.market.': '18.200.80.151'}]
ip_address_whitelist = []

# case 10：四大组件白名单
# 示例：major_components_whitelist = {"com.pax.pay.thirdpartyinvoke.PaymentService": "true", }
major_components_whitelist = {
    "com.pax.edc.init.ui.SplashActivity": "true",
    "com.pax.market.android.app.sdk.MessagerActivity": "true",
    "com.pax.maxstore.DownloadParamService": "true",
    "com.pax.market.android.app.sdk.DelayService": "true",
    "com.pax.edc.thirdpartinvoke.ui.CommInvokePaymentActivity": "true",
    "com.pax.edc.thirdpartinvoke.opensdk.OpenSdkService": "true",
    "com.pax.edc.thirdpartinvoke.nebula.NebulaPaymentService": "true"
}

# case 12：开源许可证合规检查
# 示例：license_compliance_whitelist = {'GNU Library or Lesser General Public License'}
license_compliance_whitelist = {'javacsv-2.0.jar',
                                'jaxb-api-2.2.12.jar',
                                'xpp3-1.1.4c.jar',
                                 'jakarta.annotation-api-2.0.0.jar'}

# case 13: 字体侵权检查
fonts_whitelist = {'NotoSans-SemiBold.ttf'}

# 是否跳过可选用例，False跳过检查
prohibit_screenshots = True  # case 6
prohibit_gif_animation = True  # case 9
sqlite3_encryption = True  # case 11：由于数据库加密涉及的改动范围较大，可以暂时跳过检查。
database_file_path = ['/data/data/com.pax.edc/databases/data', '/data/data/com.pax.edc/databases/auditlog',
'/data/data/com.pax.edc/databases/emv-param', '/data/data/com.pax.edc/databases/locale']
# 不在白名单内的case属于必须解决的安全性问题。
