package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientServlet extends HttpServlet {
    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final String CLIENT_PAGE_TEMPLATE = "client.html";
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientServlet(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        long id = -1;
        if (req.getPathInfo() != null) {
            id = extractIdFromRequest(req);
            paramsMap.put("clients", List.of(dbServiceClient.getClient(id).orElseThrow()));
            resp.setContentType("text/html");
            resp.getWriter().println(templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, paramsMap));
        }
        if (id == -1) {
            paramsMap.put("clients", dbServiceClient.findAll());
            resp.setContentType("text/html");
            resp.getWriter().println(templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, paramsMap));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var name = req.getParameter("name");
        var address = new Address(null, req.getParameter("address"));
        var phone = new Phone(null, req.getParameter("phone"));

        dbServiceClient.saveClient(new Client(null, name, address, List.of(phone)));
        resp.sendRedirect("/client");
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
