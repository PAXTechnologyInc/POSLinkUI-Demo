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
# case 1：apk 权限设置白名单（保留通用系统权限）
permissions_whitelist = [
    "android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.WRITE_EXTERNAL_STORAGE",
    "android.permission.CHANGE_CONFIGURATION",
    "android.permission.CAMERA",
    "android.permission.READ_PHONE_STATE",
]

# case 3：代码混淆白名单（保留通用 R$* 资源类）
obfuscation_whitelist = [
    "R$attr.smali",
    "R$drawable.smali",
    "R$id.smali",
    "R$layout.smali",
    "R$styleable.smali",
]

# case 8：IP 地址白名单（当前项目暂不配置，留空）
ip_address_whitelist = []

# case 10：四大组件白名单（当前项目暂不配置，留空）
major_components_whitelist = {}

# case 12：开源许可证合规检查（保留通用三方库白名单）
license_compliance_whitelist = {
    "javacsv-2.0.jar",
    "jaxb-api-2.2.12.jar",
    "xpp3-1.1.4c.jar",
    "jakarta.annotation-api-2.0.0.jar",
}

# case 13: 字体侵权检查（保留通用字体白名单）
fonts_whitelist = {"NotoSans-SemiBold.ttf"}

# 是否跳过可选用例标志位，保持原有含义
prohibit_screenshots = True  # case 6：屏幕截图检查
prohibit_gif_animation = True  # case 9：GIF 动画检查
sqlite3_encryption = True  # case 11：数据库加密检查
database_file_path = []
# 不在白名单内的 case 属于必须解决的安全性问题。
