package org.springframework.web.context.support;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.support.LiveBeansView;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/support/LiveBeansViewServlet.class */
public class LiveBeansViewServlet extends HttpServlet {
    @Nullable
    private LiveBeansView liveBeansView;

    @Override // javax.servlet.GenericServlet
    public void init() throws ServletException {
        this.liveBeansView = buildLiveBeansView();
    }

    protected LiveBeansView buildLiveBeansView() {
        return new ServletContextLiveBeansView(getServletContext());
    }

    @Override // javax.servlet.http.HttpServlet
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Assert.state(this.liveBeansView != null, "No LiveBeansView available");
        String content = this.liveBeansView.getSnapshotAsJson();
        response.setContentType("application/json");
        response.setContentLength(content.length());
        response.getWriter().write(content);
    }
}
