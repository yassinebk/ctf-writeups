package org.springframework.web.servlet.view.document;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/document/AbstractXlsxView.class */
public abstract class AbstractXlsxView extends AbstractXlsView {
    public AbstractXlsxView() {
        setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Override // org.springframework.web.servlet.view.document.AbstractXlsView
    /* renamed from: createWorkbook */
    protected Workbook mo1827createWorkbook(Map<String, Object> model, HttpServletRequest request) {
        return new XSSFWorkbook();
    }
}
