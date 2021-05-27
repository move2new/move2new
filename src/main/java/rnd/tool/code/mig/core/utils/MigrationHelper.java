package rnd.tool.code.mig.core.utils;

import java.nio.file.Path;

import rnd.tool.code.mig.core.AbstractMigrator;

public class MigrationHelper {

	public static void migrate(AbstractMigrator<?> mig, Path inPath, Path outPath) {
		migrate(mig, inPath, outPath, null);
	}

	public static void migrate(AbstractMigrator<?> mig, Path inPath, Path outPath, String className) {
		mig.getClassParser().setPath(inPath);
		mig.getClassParser().setClassName(className);
		mig.getClassGenerator().setPath(outPath);
		mig.migrate();
	}

	public static String migrate(AbstractMigrator<?> mig, String inSourceCode) {
		mig.getClassParser().setSourceCode(inSourceCode);
		mig.migrate();
		String outSourceCode = mig.getClassGenerator().getSourceCode();
		return outSourceCode;
	}

}
