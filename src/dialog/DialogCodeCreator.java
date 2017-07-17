package dialog;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;


public class DialogCodeCreator extends WriteCommandAction.Simple {

    private Project project;
    private PsiFile file;
    private PsiClass targetClass;
    private PsiElementFactory factory;

    public DialogCodeCreator(Project project, PsiClass targetClass, PsiElementFactory factory, PsiFile... files) {
        super(project, files);
        this.project = project;
        this.file = files[0];
        this.targetClass = targetClass;
        this.factory = factory;
    }

    @Override
    protected void run() throws Throwable {
        StringBuffer field = new StringBuffer();
        field.append("private android.support.v7.app.AlertDialog alertDialog;");
        targetClass.add(factory.createFieldFromText(field.toString(), targetClass));
        StringBuilder builder = new StringBuilder();
        builder.append("public void showDialog(){");
        builder.append("if(alertDialog == null){");
        builder.append("android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);");
        builder.append("builder.setTitle(\"标题\")\n");
        builder.append(".setMessage(\"内容\")\n");
        builder.append(".setPositiveButton(\"确定\", new android.content.DialogInterface.OnClickListener() {\n" +
                "@Override\n" +
                "public void onClick(DialogInterface dialog, int which) {\n" +
                "\t\n" +
                "}" +
                "})\n");
        builder.append(".setNegativeButton(\"取消\", new DialogInterface.OnClickListener() {\n" +
                "@Override\n" +
                "public void onClick(DialogInterface dialog, int which) {\n" +
                "\t\n" +
                "}" +
                "});\n");
        builder.append("alertDialog = builder.create();");
        builder.append("}");
        builder.append("alertDialog.setCanceledOnTouchOutside(false);");
        builder.append("alertDialog.show();");
        builder.append("}");
        PsiMethod method = factory.createMethodFromText(builder.toString(), targetClass);
        targetClass.add(method);
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(targetClass);
    }
}
