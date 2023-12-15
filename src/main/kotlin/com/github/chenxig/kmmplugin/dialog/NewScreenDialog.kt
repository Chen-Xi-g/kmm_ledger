package com.github.chenxig.kmmplugin.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * 创建新的Screen弹窗
 *
 * @author 高国峰
 * @date 2023/12/14-12:23
 */
class NewScreenDialog(
    project: Project,
    private val path: String
) : DialogWrapper(project) {

    private val pathTextField = JBTextField()
    private val descTextField = JBTextField()
    private val classNameTextField = JBTextField()
    private val isCreateModel = JBCheckBox("Generator Model", true)
    private val isCreateViewModel = JBCheckBox("Generator ViewModel", true)

    init {
        title = "新建屏幕"
        init()
    }

    override fun createCenterPanel(): JComponent {
        pathTextField.text = path
        classNameTextField.text = "Main"
        val panel = FormBuilder.createFormBuilder()
            .addLabeledComponent("Path: ", pathTextField)
            .addLabeledComponent("Class Name:", classNameTextField)
            .addLabeledComponent("Class Description Name:", descTextField)
            .addLabeledComponent("", isCreateModel)
            .addLabeledComponent("", isCreateViewModel)
            .panel
        return JPanel(BorderLayout()).apply { add(panel, BorderLayout.NORTH) }
    }

    fun getDesc(): String {
        return descTextField.text
    }

    fun getClassName(): String {
        return classNameTextField.text
    }

    fun isCreateModel(): Boolean {
        return isCreateModel.isSelected
    }

    fun isCreateViewModel(): Boolean {
        return isCreateViewModel.isSelected
    }

    fun getPath(): String {
        return pathTextField.text
    }

}