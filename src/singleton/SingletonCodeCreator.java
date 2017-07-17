package singleton;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;


public class SingletonCodeCreator extends WriteCommandAction.Simple {

    private Project project;
    private PsiFile file;
    private PsiClass targetClass;
    private PsiElementFactory factory;

    public SingletonCodeCreator(Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        super(project, files);
        this.project = project;
        this.file = files[0];
        this.targetClass = targetClass;
        this.factory = factory;
    }

    @Override
    protected void run() throws Throwable {
        StringBuilder builder = new StringBuilder();
        String clazzName = targetClass.getName();
        clazzName = clazzName==null?"ok":clazzName;
        StringBuffer field = new StringBuffer();
        field.append("private static ").append(clazzName).append(" singleton;\t\n");
        targetClass.add(factory.createFieldFromText(field.toString(), targetClass));
        builder.append("public static ").append(clazzName).append(" get(){\n");
        builder.append("if(null == ").append("singleton").append("){\n");
        builder.append("synchronized (").append(clazzName).append(".class){\n");
        builder.append("if(null == ").append("singleton").append("){\n");
        builder.append("singleton = new ").append(clazzName).append("();\n");
        builder.append("}\n");
        builder.append("}\n");
        builder.append("}\n");
        builder.append("return singleton;");
        builder.append("}");
        // 将代码添加到当前类里
        targetClass.add(factory.createMethodFromText(builder.toString(), targetClass));
        // 导入需要的类
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(targetClass);
    }
}
