package createfield;

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
 * 创建成员变量的别名，用于数据库操作
 */
public class CreateFieldAction extends BaseGenerateAction {

    public CreateFieldAction(){
        super(null);
    }

    public CreateFieldAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取编辑器中的文件
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if(project == null || editor == null)
            return ;
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if(file == null)
            return ;
        // 获取当前类
        PsiClass targetClass = getTargetClass(editor, file);
        // 获取元素操作的工厂类
        PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
        // 生成代码
        new FieldCodeCreator(project, targetClass, factory, file).execute();
    }
}
