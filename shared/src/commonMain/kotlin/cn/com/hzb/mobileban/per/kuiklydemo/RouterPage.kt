package cn.com.hzb.mobileban.per.kuiklydemo

import com.tencent.kuikly.core.annotations.Page
import com.tencent.kuikly.core.base.*
import com.tencent.kuikly.core.directives.vif
import com.tencent.kuikly.core.module.RouterModule
import com.tencent.kuikly.core.module.SharedPreferencesModule
import com.tencent.kuikly.core.nvi.serialization.json.JSONObject
import com.tencent.kuikly.core.utils.urlParams
import com.tencent.kuikly.core.views.*
import com.tencent.kuikly.core.views.compose.Button
import com.tencent.kuikly.core.reactive.handler.*
import cn.com.hzb.mobileban.per.kuiklydemo.base.BasePager
import cn.com.hzb.mobileban.per.kuiklydemo.base.bridgeModule

@Page("router", supportInLocal = true)
internal class RouterPage : BasePager() {

    var inputText: String = ""
    lateinit var inputRef: ViewRef<InputView>

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            attr {
                allCenter()
                backgroundColor(Color.WHITE)
            }
                // 计算布局参数
                val pkTotalWidth = 80f   // PK区域总宽度（包含被遮挡部分）
                val buttonBaseWidth = (ctx.pagerData.pageViewWidth - pkTotalWidth) / 2 // 左右按钮基础宽度

                // PK按钮容器
                View {
                    attr {
                        width(ctx.pagerData.pageViewWidth)
                        height(48f)
                        flexDirectionRow()
                        overflow(true) // 允许内容溢出（用于实现倾斜重叠效果）
                        justifyContentSpaceBetween()
                        borderRadius(32f)
                        marginTop(12f)
                    }

                    // 左边按钮（红色）
                    View {
                        attr {
                            width(buttonBaseWidth)
                            height(48f)
                            backgroundColor(0xFFE96664)
                            justifyContentCenter()
                            alignItemsCenter()
                            border(
                                Border(
                                    2f,
                                    BorderStyle.SOLID,
                                    Color(0xFFE96664)
                                )
                            )
                        }


                        Text {
                            attr {
                                text("111")
                                fontSize(16f)
                                color(Color.WHITE)
                            }
                        }
                    }

                    // 红色倾斜层（视觉连接效果，连接红色按钮和中间PK区域）
                    View {
                        attr {
                            width(21f)
                            height(50f)
                            backgroundColor(0xFFE96664)
                            transform(skew = Skew(-24f, 0f), anchor = Anchor(0f,0f))
                            positionAbsolute()
                            top(-1f) // 确保完全覆盖
                            left((ctx.pagerData.pageViewWidth - pkTotalWidth) / 2) // 定位到红色区域右侧
                            zIndex(-1)
                        }
                    }

                    // 中间PK图标区域
                    View {
                        attr {
                            width(pkTotalWidth)
                            height(48f)
                            justifyContentCenter()
                            alignItemsCenter()
                            positionAbsolute()
                            right(buttonBaseWidth)
                            // 上下边框连接左右按钮（红色上边框，蓝色下边框）
                            borderTop(
                                Border(
                                    2f,
                                    BorderStyle.SOLID,
                                    Color(0xFFE96664)
                                )
                            )
                            borderBottom(
                                Border(
                                    2f,
                                    BorderStyle.SOLID,
                                    Color(0xFF5199FF)
                                )
                            )
                        }

                    }

                    // 蓝色倾斜背景层（连接中间PK区域和蓝色按钮）
                    View {
                        attr {
                            width(21f)
                            height(48f)
                            backgroundColor(0xFF5199FF)
                            transform(skew = Skew(-24f, 0f), anchor = Anchor(0f,0f))
                            positionAbsolute()
                            right(ctx.pagerData.pageViewWidth / 2 - pkTotalWidth / 2 - 21f)
                            zIndex(-1)
                        }
                    }

                    // 右边按钮（蓝色）
                    View {
                        attr {
                            width(buttonBaseWidth)
                            height(48f)
                            justifyContentCenter()
                            alignItemsCenter()
                            borderRadius(0f, 24f, 0f, 24f) // 右侧圆角
                            backgroundColor(0xFF5199FF)
                        }
                        Text {
                            attr {
                                text("2222")
                                fontSize(16f)
                                color(Color.WHITE)
                            }
                        }

                    }
                }
            }
    }

    override fun created() {
        super.created()
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        val cacheInputText =
            acquireModule<SharedPreferencesModule>(SharedPreferencesModule.MODULE_NAME).getItem(
                CACHE_KEY
            )
        if (cacheInputText.isNotEmpty()) {
            inputRef.view?.setText(cacheInputText)
        }
    }

    private fun jumpPage(inputText: String) {
        val params = urlParams("pageName=$inputText")
        val pageData = JSONObject()
        params.forEach {
            pageData.put(it.key, it.value)
        }
        val pageName = pageData.optString("pageName")
        acquireModule<RouterModule>(RouterModule.MODULE_NAME).openPage(pageName, pageData)
    }

    companion object {
        const val PLACEHOLDER = "输入pageName"
        const val TIP = "输入规则：router 或者 router&key=value (&后面为页面参数)"
        const val CACHE_KEY = "router_last_input_key2"
        const val BG_URL =
            "https://sqimg.qq.com/qq_product_operations/kan/images/viola/viola_bg.jpg"
        const val LOGO = "https://vfiles.gtimg.cn/wuji_dashboard/wupload/xy/starter/62394e19.png"
        const val JUMP_TEXT = "跳转"
        const val TEXT_KEY = "text"
        const val TITLE = "Kuikly页面路由"
        private const val AAR_MODE_TIP = "如：router 或者 router&key=value （&后面为页面参数）"
    }

}

internal class RouterNavigationBar : ComposeView<RouterNavigationBarAttr, ComposeEvent>() {
    override fun createEvent(): ComposeEvent {
        return ComposeEvent()
    }

    override fun createAttr(): RouterNavigationBarAttr {
        return RouterNavigationBarAttr()
    }

    override fun body(): ViewBuilder {
        val ctx = this
        return {
            View {
                attr {
                    paddingTop(ctx.pagerData.statusBarHeight)
                    backgroundColor(Color.WHITE)
                }
                // nav bar
                View {
                    attr {
                        height(44f)
                        allCenter()
                    }

                    Text {
                        attr {
                            text(ctx.attr.title)
                            fontSize(17f)
                            fontWeightSemisolid()
                            backgroundLinearGradient(
                                Direction.TO_BOTTOM,
                                ColorStop(Color(0xFF23D3FD), 0f),
                                ColorStop(Color(0xFFAD37FE), 1f)
                            )

                        }
                    }

                }

                vif({ !ctx.attr.backDisable }) {
                    Image {
                        attr {
                            absolutePosition(
                                top = 12f + getPager().pageData.statusBarHeight,
                                left = 12f,
                                bottom = 12f,
                                right = 12f
                            )
                            size(10f, 17f)
                            src("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAASBAMAAAB/WzlGAAAAElBMVEUAAAAAAAAAAAAAAAAAAAAAAADgKxmiAAAABXRSTlMAIN/PELVZAGcAAAAkSURBVAjXYwABQTDJqCQAooSCHUAcVROCHBiFECTMhVoEtRYA6UMHzQlOjQIAAAAASUVORK5CYII=")
                        }
                        event {
                            click {
                                getPager().acquireModule<RouterModule>(RouterModule.MODULE_NAME)
                                    .closePage()
                            }
                        }
                    }
                }

            }
        }
    }
}

internal class RouterNavigationBarAttr : ComposeAttr() {
    var title: String by observable("")
    var backDisable = false
}

internal fun ViewContainer<*, *>.RouterNavBar(init: RouterNavigationBar.() -> Unit) {
    addChild(RouterNavigationBar(), init)
}