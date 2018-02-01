package com.thomastige.jiraawesomeificator.ui;

import com.thomastige.jiraawesomeificator.controllers.web.ReportController;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;


@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

    @Autowired
    private ReportController reportController;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout root = new VerticalLayout();
        setContent(root);

        TextArea textArea = new TextArea();
        textArea.setSizeFull();
        HorizontalLayout reportButtons = new HorizontalLayout();
        reportButtons.addComponent(new Button("Calendar Report",
                e -> textArea.setValue((String) reportController.getCalendarReport().getBody())
        ));
        reportButtons.addComponent(new Button("Hours Report",
                e -> textArea.setValue((String) reportController.getHoursReport().getBody())
        ));
        reportButtons.addComponent(new Button("Metrics Report",
                e -> textArea.setValue((String) reportController.getMetricsReport().getBody())
        ));
//        reportButtons.addComponent(new Button("Download All Notes",
//                e -> textArea.setValue((String) reportController.getNoteArchive().getBody())
//        ));
        root.addComponents(reportButtons, textArea);
    }
}
