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
# 用例标题: Application Permission Security Check (应用申请的权限安全检测)
# 示例: permissions_whitelist = ["android.permission.READ_EXTERNAL_STORAGE"]
permissions_whitelist = [
                           "android.permission.READ_EXTERNAL_STORAGE",
                             "android.permission.WRITE_EXTERNAL_STORAGE",
                             "android.permission.CHANGE_CONFIGURATION",
                             "android.permission.CAMERA",
                             "android.permission.READ_PHONE_STATE",
                             "com.paxus.pay.poslinkui.demo.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION",
                             ]

# 用例标题: Code Obfuscation Check (代码混淆检查)
# 示例: obfuscation_whitelist = True，是否跳过可选用例，False跳过检查
obfuscation_whitelist = [
                            "R$attr.smali",
                            "R$drawable.smali",
                            "R$id.smali",
                            "R$layout.smali",
                            "R$styleable.smali",
                        ]

# 用例标题: Application Integrity Validation & Icon Replace (完整性校验检查)
# 示例: completeness_whitelist_flag = True，是否跳过可选用例，False跳过检查
completeness_whitelist_flag = True

# 用例标题: Check ip address (外部IP地址安全检测)
# 示例: ip_address_whitelist = [{'com.pax.market.': '18.200.80.151'}]
ip_address_whitelist = []

# 用例标题: Android Core Component Security Check (四大组件安全检测)
# 示例: major_components_whitelist = {"com.pax.pay.thirdpartyinvoke.PaymentService": "true", }
major_components_whitelist = {
     "com.paxus.pay.poslinkui.demo.MainActivity": "true",
     "com.paxus.pay.poslinkui.demo.entry.EntryActivity": "true"
}

# 用例标题: Dependency Licence Detection (开源许可证合规检查)
# 示例: license_compliance_whitelist = {'GNU Library or Lesser General Public License'}
license_compliance_whitelist = {}

# 用例标题: Font License Compliance Check (字体库侵权检查)
# 示例: fonts_whitelist = {'aaa.ttf'}
fonts_whitelist = {}

# 用例标题: Fragment Injection Check (Fragment注入检查)
# 示例: Fragment_whitelist_flag = True，是否跳过可选用例，False跳过检查
Fragment_whitelist_flag = True

# 用例标题: Activity Hijacking Check (Activity界面劫持检查)
# 示例: Activity_whitelist_flag = True，是否跳过可选用例，False跳过检查
Activity_whitelist_flag = True

# 用例标题: Dynamic DEX File Security Validation Check (动态加载DEX文件检查)
# 示例: dex_dynamic_loading_whitelist = True，是否跳过可选用例，False跳过检查
dex_dynamic_loading_whitelist_flag = True

# 用例标题: Module Comparison Between SonarQube Configuration File and Gradle Settings File
# (sonar文件扫描模块与gradle文件模块对比检查)
# 示例: sonar_whitelist = ['gradlew', 'gradlew.bat', 'gradle-wrapper.jar']
sonar_whitelist = []

# 用例标题: Application Anti-Screenshot and Anti-Screen-Recording Check (应用防截屏防录屏检查)
# 示例: prohibit_screenshots = True，是否跳过可选用例，False跳过检查
prohibit_screenshots = True

# 用例标题: GIF Animation Detection (GIF动态图片检测)
# 示例: prohibit_gif_animation = True，是否跳过可选用例，False跳过检查
prohibit_gif_animation = False

# 用例标题: Database Encryption Check (数据库加密检查)
# 示例: sqlite3_encryption = True，是否跳过可选用例，False跳过检查
# 由于数据库加密涉及的改动范围较大，可以暂时跳过检查。
sqlite3_encryption = False
# 示例: database_file_path = ['/data/data/com.pax.edc.ui1/databases/data.db']，将需要检查的数据库文件路径填进去即可。
# 提示: 数据库文件路径为必填项，不填则会导致检查失败。
database_file_path = []

# 用例标题: Check the apk name and package and version number (检查apk名称、apk包名和版本号)
# 示例: check_apk_name_and_package = True，是否跳过可选用例，False跳过检查
check_apk_name_and_package = False

# 用例标题: Comprehensive inspection of Bitmap usage (Bitmap内存优化检查)
# 示例：bitmap_whitelist = ['ResourceUtil.java','logo_halyk_launch.png'] 过大的图片文件和有问题的代码文件都放置在该列表里面
bitmap_whitelist = []

# 不在白名单内的case属于必须解决的安全性问题。
