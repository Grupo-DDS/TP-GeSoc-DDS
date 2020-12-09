package controllers;


import static app.Application.bookDao;
import static app.JsonUtil.dataToJson;
import static app.RequestUtil.clientAcceptsHtml;
import static app.RequestUtil.clientAcceptsJson;
import static app.RequestUtil.getParamIsbn;

import java.util.HashMap;

import app.Path;
import app.ViewUtil;
import app.dominio.Book;
import spark.Request;
import spark.Response;
import spark.Route;

public class BookController {

    public static Route fetchAllBooks = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            //obtencion, generacion del Modelo (MVC)
            HashMap<String, Object> model = new HashMap<>();
            model.put("books", bookDao.getAllBooks());
            //actualiza la Vista (MVC) que es un HTML
            return ViewUtil.render(request, model, Path.Template.BOOKS_ALL);
        }
        if (clientAcceptsJson(request)) {
            //actualiza la Vista, que es un JSON
            return dataToJson(bookDao.getAllBooks());
        }
        //actualiza la Vista con un mensaje de error
        return ViewUtil.notAcceptable.handle(request, response);
    };

    public static Route fetchOneBook = (Request request, Response response) -> {
        LoginController.ensureUserIsLoggedIn(request, response);
        if (clientAcceptsHtml(request)) {
            HashMap<String, Object> model = new HashMap<>();
            Book book = bookDao.getBookByIsbn(getParamIsbn(request));
            model.put("book", book);
            return ViewUtil.render(request, model, Path.Template.BOOKS_ONE);
        }
        if (clientAcceptsJson(request)) {
            return dataToJson(bookDao.getBookByIsbn(getParamIsbn(request)));
        }
        return ViewUtil.notAcceptable.handle(request, response);
    };
}
