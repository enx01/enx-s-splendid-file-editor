package xyz.zzzoozo.enx.ide.elements.panes;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.jediterm.pty.PtyProcessTtyConnector;
import com.jediterm.terminal.CursorShape;
import com.jediterm.terminal.TtyConnector;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.settings.DefaultSettingsProvider;
import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;

public class UtilsPane extends JPanel {

    private JediTermWidget termWidget;

    public UtilsPane(Frame owner) {
        setLayout(new BorderLayout());
        JTabbedPane content = new JTabbedPane();

        termWidget = createTermWidget();

        termWidget.addListener(terminalWidget -> {
            termWidget.close();
        });

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(termWidget);

        content.addTab("Terminal", container);
        content.addTab("Debugger", new JPanel());
        content.addTab("Logs", new JPanel());

        add(content);
    }

    public JediTermWidget getWidget() {
        return this.termWidget;
    }

    private static JediTermWidget createTermWidget() {
        JediTermWidget widget = new JediTermWidget(80, 20, new DefaultSettingsProvider());
        widget.getTerminalPanel().setDefaultCursorShape(CursorShape.BLINK_UNDERLINE);
        widget.setTtyConnector(createTtyConnector());
        widget.start();
        return widget;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    private static TtyConnector createTtyConnector() {
        try {
            Map<String, String> envs = System.getenv();
            String[] command;
            if (isWindows()) {
                command = new String[] { "cmd.exe" };
            } else {
                command = new String[] { "/bin/bash", "--login" };
                envs = new HashMap<>(System.getenv());
                envs.put("TERM", "xterm-256color");
            }

            PtyProcess process = new PtyProcessBuilder().setCommand(command).setEnvironment(envs).start();
            return new PtyProcessTtyConnector(process, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
