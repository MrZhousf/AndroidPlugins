package createfield;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;


public class FieldCodeCreator extends WriteCommandAction.Simple {

    private Project project;
    private PsiFile file;
    private PsiClass targetClass;
    private PsiElementFactory factory;

    public FieldCodeCreator(Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        super(project, files);
        this.project = project;
        this.file = files[0];
        this.targetClass = targetClass;
        this.factory = factory;
    }

    @Override
    protected void run() throws Throwable {
        PsiField[] fields = targetClass.getAllFields();
        if(fields.length > 0){
            for (PsiField field : fields){
                final String name = field.getName();
                if(name != null && !name.startsWith("_")){
                    StringBuilder builder = new StringBuilder();
                    builder.append("public final static String _").append(name).append(" = \"").append(name).append("\"").append(";\n");
                    targetClass.add(factory.createFieldFromText(builder.toString(), targetClass));
                }
            }
        }
        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(targetClass);
    }



}
