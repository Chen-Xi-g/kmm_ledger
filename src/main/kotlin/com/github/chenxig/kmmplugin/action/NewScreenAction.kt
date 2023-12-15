package com.github.chenxig.kmmplugin.action

import com.github.chenxig.kmmplugin.dialog.NewScreenDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.apache.commons.lang.time.DateFormatUtils

/**
 * 创建新的Screen
 *
 * @author 高国峰
 * @date 2023/12/14-12:21
 */
class NewScreenAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.apply {
            val path = getPath(e)
            // 获取当前选中项的 PsiDirectory
            val psiDirectory: PsiDirectory? = getCurrentPsiDirectory(e)
            if (psiDirectory != null) {
                val newScreenDialog = NewScreenDialog(this, path)
                val isOk = newScreenDialog.showAndGet()
                if (isOk) {
                    // 用户点击了确定, 获取用户输入和选择的内容
                    val desc = newScreenDialog.getDesc()
                    val className = newScreenDialog.getClassName()
                    val isCreateModel = newScreenDialog.isCreateModel()
                    val isCreateViewModel = newScreenDialog.isCreateViewModel()
                    val packageName = getPackage(newScreenDialog.getPath())
                    val createDate =
                        DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd-HH:mm")

                    // 根据用户输入调用相应的函数生成类
                    if (isCreateModel) {
                        val code = generateModelClass(packageName, className, desc, createDate)
                        saveCodeToFile(this, psiDirectory, "${className}Model", code)
                    }
                    if (isCreateViewModel) {
                        val code = generateViewModelClass(packageName, className, desc, createDate)
                        saveCodeToFile(this, psiDirectory, "${className}VM", code)
                    }
                    // 生成带描述的类
                    val code = generateScreenClass(packageName, className, desc, createDate)
                    saveCodeToFile(this, psiDirectory, "${className}Screen", code)
                }
            }
        }
    }

    private fun getPackage(path: String): String {
        // 从路径中获取包名, 规则是/src/*/kotlin/之后的路径, *可能是任何字符, 但是不能包含/, 例如: /Users/alvin/AndroidStudioProjects/examples/kmm_ledger/composeApp/src/commonMain/kotlin/ui/screen/demo匹配出来的是ui.screen.demo
        val regex = Regex("/src/[^/]+/kotlin/(.+)")
        val matchResult = regex.find(path)
        val packageName = matchResult?.groupValues?.get(1)
        return packageName?.replace("/", ".") ?: ""
    }

    private fun getPath(e: AnActionEvent): String {
        val psiFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        return psiFile?.path ?: ""
    }

    private fun getCurrentPsiDirectory(e: AnActionEvent): PsiDirectory? {
        // 获取当前选中项
        val psiFile: PsiFile? = e.getData(PlatformDataKeys.PSI_FILE)
        if (psiFile != null && psiFile.isDirectory) {
            // 如果当前选中项是目录，直接返回
            return psiFile as PsiDirectory
        } else {
            // 获取当前选中项的 PsiElement
            val psiElement: PsiElement? =
                e.getData(PlatformDataKeys.PSI_ELEMENT)
            if (psiElement != null) {
                // 如果当前选中项不是目录，则尝试获取父目录
                return (psiElement as? PsiDirectory) ?: (psiElement.parent as? PsiDirectory)
            }
        }

        // 如果当前选中项不是目录，则尝试获取父目录
        return psiFile?.parent
    }

    private fun saveCodeToFile(
        project: Project,
        psiDirectory: PsiDirectory,
        fileName: String,
        code: String
    ) {
        val virtualDirectory: VirtualFile? = psiDirectory.virtualFile
        if (virtualDirectory != null) {
            // 创建一个新的 VirtualFile
            val virtualFile = virtualDirectory.createChildData(this, "$fileName.kt")
            // 将代码写入文件
            virtualFile.getOutputStream(this).use { it.write(code.toByteArray()) }

            // 刷新文件系统，以确保文件显示在IDE中
            LocalFileSystem.getInstance()
                .refreshAndFindFileByIoFile(virtualFile.toNioPath().toFile())

            // 打开新创建的文件
            FileEditorManager.getInstance(project).openFile(virtualFile, true)
        }
    }

    private fun generateModelClass(
        packageName: String,
        className: String,
        desc: String,
        createDate: String
    ): String {
        return """
        package $packageName

        import core.navigation.UiEffect
        import core.navigation.UiEvent
        import core.navigation.UiState

        /**
         * $desc 状态
         *
         * @author 高国峰
         * @date $createDate
         */
        data class ${className}State(
            val error: String? = null,
            val isLoading: Boolean = false
        ): UiState

        /**
         * $desc 事件
         */
        sealed interface ${className}Event: UiEvent

        /**
         * $desc 效果
         */
        sealed interface ${className}Effect: UiEffect
    """.trimIndent()
    }


    private fun generateScreenClass(
        packageName: String,
        className: String,
        desc: String,
        createDate: String
    ): String {
        return """
        package $packageName

        import androidx.compose.foundation.layout.fillMaxSize
        import androidx.compose.material3.Surface
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.collectAsState
        import androidx.compose.runtime.getValue
        import androidx.compose.ui.Modifier
        import platform.WindowInfo
        import platform.rememberWindowInfo
        import platform.safeArea
        
        /**
         * $desc 屏幕
         *
         * @author 高国峰
         * @date $createDate
         */
        @Composable
        fun ${className}Screen(
            component: ${className}VM
        ) {
            val state by component.state.collectAsState()
            val windowInfo = rememberWindowInfo()
            Surface(
                modifier = Modifier.fillMaxSize()
                    .safeArea()
            ) {
                if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) {
                    ${className}Content(state, component::onEvent)
                } else {
                    ${className}ContentLarge(state, component::onEvent)
                }
            }
        }
        
        /**
         * 竖屏，窄屏布局
         */
        @Composable
        private fun ${className}Content(
            state: ${className}State,
            onEvent: (${className}Event) -> Unit
        ) {
        
        }
        
        /**
         * 横屏，宽屏布局
         */
        @Composable
        private fun ${className}ContentLarge(
            state: ${className}State,
            onEvent: (${className}Event) -> Unit
        ) {
        
        }
    """.trimIndent()
    }

    private fun generateViewModelClass(
        packageName: String,
        className: String,
        desc: String,
        createDate: String
    ): String {
        return """
        package $packageName

        import com.arkivanov.decompose.ComponentContext
        import core.navigation.BaseComponent

        /**
         * $desc VM
         *
         * @author 高国峰
         * @date $createDate
         */
        class ${className}VM(
            componentContext: ComponentContext
        ) : BaseComponent<${className}State, ${className}Event, ${className}Effect>(componentContext) {
            override fun initialState(): ${className}State {
                return ${className}State()
            }

            override fun onEvent(event: ${className}Event) {
            }
        }
    """.trimIndent()
    }


}