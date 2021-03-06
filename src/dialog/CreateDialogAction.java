package dialog;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

/**
 * 创建确认对话框
 */
public class CreateDialogAction extends BaseGenerateAction {

    public CreateDialogAction() {
        super(null);
    }

    public CreateDialogAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取编辑器中的文件
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if(editor == null || project == null)
            return ;
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if(file == null)
            return ;
        // 获取当前类
        PsiClass targetClass = getTargetClass(editor, file);
        // 获取元素操作的工厂类
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        // 生成代码
        new DialogCodeCreator(project, targetClass, factory, file).execute();
    }
}
