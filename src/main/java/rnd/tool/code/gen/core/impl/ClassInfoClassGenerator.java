package rnd.tool.code.gen.core.impl;

import java.io.Writer;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rnd.tool.code.gen.core.AbstractClassGenerator;
import rnd.tool.code.model.ClassInfo;
import rnd.tool.code.model.ClassInfo.Type;
import rnd.tool.code.model.ElementInfo;
import rnd.tool.code.model.MethodInfo;

public class ClassInfoClassGenerator extends AbstractClassGenerator<ClassInfo> {

	public static final String CLASS_SUFFIX = "Default";

	private Logger logger = LogManager.getLogger(getClass().getName());

	@Override
	public String getClassSuffix() {
		return CLASS_SUFFIX;
	}

	protected String getSourcePackageName(ClassInfo sourceClass) {
		String packageName = "";
		if (sourceClass.getPackageName() != null) {
			packageName = sourceClass.getPackageName();
		}
		return packageName;
	}

	protected String getSourceClassName(ClassInfo classInfo) {
		return classInfo.getName();
	}

	protected void generateClass(Writer writer, ClassInfo classInfo) {
		writePackageName(writer, classInfo);
		writeImports(writer, classInfo);
		writeClass(writer, classInfo);
	}

	protected void writePackageName(Writer writer, ClassInfo clazzInfo) {
		final String packName = clazzInfo.getPackageName();
		if (packName != null && packName.length() != 0) {
			writeln(writer, "package " + packName + ";");
			writeln(writer);
			logger.info("Writing package name : " + packName);
		}
	}

	protected void writeImports(Writer writer, ClassInfo classInfo) {
		logger.info("Writing imports");
		classInfo.getImports().stream().forEach(imp -> writeln(writer, imp));
		writeln(writer);
	}

	protected void writeClass(Writer writer, ClassInfo classInfo) {

		logger.info("Writing class name : " + getTargetClassName(classInfo.getName()));
		write(writer, "public");

		if (classInfo.getType() == Type.Class) {
			write(writer, " class ");
		} else {
			write(writer, " interface ");
		}
		write(writer, getTargetClassName(classInfo.getName()));
		writeExtensions(writer, classInfo);
		writeln(writer, " {");
		writeFields(writer, classInfo);
		writeMethods(writer, classInfo);
		writeln(writer, "}");
	}

	protected void writeExtensions(Writer writer, ClassInfo classInfo) {
		String extensions = classInfo.getExtensions().stream().map(ClassInfo::getName).reduce(String::join)
				.orElse(null);
		if (extensions != null) {
			logger.info("Writing extensions");
			write(writer, " extends " + extensions);
		}
	}

	protected void writeFields(Writer writer, ClassInfo classInfo) {
		writeln(writer);

		List<ElementInfo> fields = classInfo.getFields();
		if (!fields.isEmpty()) {
			for (ElementInfo field : fields) {

				writeField(writer, classInfo, field);
			}
			writeln(writer);
		}
	}

	protected void writeField(Writer writer, ClassInfo classInfo, ElementInfo field) {
		logger.info("Writing field :" + field.getName());
		write(writer, "\t");

		if (field.getModifier() != null && !field.getModifier().trim().isEmpty()) {
			write(writer, field.getModifier() + " ");
		}

		write(writer, field.getType() + " " + field.getName());

		if (field.getInit() != null && !field.getInit().trim().isEmpty()) {
			write(writer, " = " + field.getInit());
		}

		writeln(writer, ";");
	}

	protected void writeMethods(Writer writer, ClassInfo classInfo) {
		List<? extends MethodInfo> methods = classInfo.getMethods();
		for (MethodInfo minfo : methods) {
			writeMethod(writer, classInfo, minfo);
			logger.info("Writing method :" + minfo.getName());
		}
	}

	protected void writeMethod(Writer writer, ClassInfo classInfo, MethodInfo minfo) {
		if (minfo != null) {
			String retType = minfo.getType();
			write(writer, "\tpublic " + (retType != null ? retType : "void") + " ");
			write(writer, minfo.getName() + "(");
			writeParams(writer, minfo);
			String excep = minfo.getExceptions() != null ? ("throws " + minfo.getExceptions()) : "";
			write(writer, ") " + excep);
			writeBody(writer, minfo);
		}

	}

	protected void writeParams(Writer writer, MethodInfo minfo) {
		List<ElementInfo> params = minfo.getParams();
		for (int i = 0; i < params.size(); i++) {
			ElementInfo param = params.get(i);
			write(writer, i == 0 ? "" : ", ");
			write(writer, param.getType() + " " + param.getName());
		}
	}

	protected void writeBody(Writer writer, MethodInfo minfo) {
		String body = minfo.getDefinition();
		writeln(writer, body);
		writeln(writer);
	}

}