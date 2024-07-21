package org.springframework.web.servlet.view.document;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.AbstractView;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/document/AbstractXlsView.class */
public abstract class AbstractXlsView extends AbstractView {
    protected abstract void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    public AbstractXlsView() {
        setContentType("application/vnd.ms-excel");
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Workbook workbook = mo1827createWorkbook(model, request);
        buildExcelDocument(model, workbook, request, response);
        response.setContentType(getContentType());
        renderWorkbook(workbook, response);
    }

    /* renamed from: createWorkbook */
    protected Workbook mo1827createWorkbook(Map<String, Object> model, HttpServletRequest request) {
        return new HSSFWorkbook();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void renderWorkbook(Workbook workbook, HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
    }
}
