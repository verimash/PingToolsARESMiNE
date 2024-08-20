import tool.PingTool;

import java.io.File;

public final class Main {

    public static void main(String[] args) {
        final File[] files = new File(".").listFiles();

        if (files != null) {
            for (final File file : files) {
                final String fileName = file.getName();

                if (file.isFile() && fileName.endsWith(".bat") && !fileName.equals("start.bat")) {
                    file.delete();
                }
            }
        }

        CheckUpdates.execute();
        final var tool = new PingTool();
        while (true) {
            tool.update();
        }
    }
}