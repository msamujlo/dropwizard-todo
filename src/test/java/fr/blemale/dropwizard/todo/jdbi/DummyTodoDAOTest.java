package fr.blemale.dropwizard.todo.jdbi;

import com.google.common.base.Optional;
import fr.blemale.dropwizard.todo.core.Todo;
import fr.blemale.dropwizard.todo.core.TodoBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DummyTodoDAOTest {
    private DummyTodoDAO dummyTodoDAO;

    @Before
    public void setUp() {
        dummyTodoDAO = new DummyTodoDAO();
    }

    @After
    public void cleanUp() {
        dummyTodoDAO = null;
    }

    @Test
    public void createTodo() {
        Todo newTodo = TodoBuilder.aTodo(1L, "title").withContent(Optional.of("content")).build();
        Todo expectedTodo = TodoBuilder.aTodo(newTodo).withId(0L).build();

        Todo currentTodo = dummyTodoDAO.createTodo(newTodo);

        assertThat("creating a new Todo returns the same todo with a valid id", currentTodo, is(expectedTodo));
    }

    @Test
    public void updateTodoWithAnExistingTodo() {
        Todo newTodo = TodoBuilder.aTodo(0L, "title").withContent(Optional.of("content")).build();
        Todo updatedTodo = TodoBuilder.aTodo(newTodo).withTitle("updatedTodo").withContent(Optional.of("updatedContent")).build();
        Optional<Todo> expectedTodo = Optional.of(updatedTodo);

        dummyTodoDAO.createTodo(newTodo);
        Optional<Todo> currentTodo = dummyTodoDAO.updateTodo(updatedTodo);

        assertThat("updating an existing Todo returns a present updated Todo", currentTodo, is(expectedTodo));
    }


    @Test
    public void updateTodoWithANonExistingTodo() {
        Todo updatedTodo = TodoBuilder.aTodo(0L, "updatedTitle").withContent(Optional.of("updatedContent")).build();
        Optional<Todo> expectedTodo = Optional.absent();

        Optional<Todo> currentTodo = dummyTodoDAO.updateTodo(updatedTodo);

        assertThat("updating a non existing Todo returns an absent Todo", currentTodo, is(expectedTodo));
    }

    @Test
    public void getTodosWithNoTodos() {
        List<Todo> expectedTodos = Collections.EMPTY_LIST;

        List<Todo> currentTodos = dummyTodoDAO.getTodos();

        assertThat("getting todos when any todo created returns an empty list", currentTodos, is(expectedTodos));
    }

    @Test
    public void getTodosWhithTodos() {
        Todo newTodo0 = TodoBuilder.aTodo(0L, "title0").withContent(Optional.of("content0")).build();
        Todo newTodo1 = TodoBuilder.aTodo(1L, "title1").withContent(Optional.of("content1")).build();

        dummyTodoDAO.createTodo(newTodo0);
        dummyTodoDAO.createTodo(newTodo1);
        List<Todo> currentTodos = dummyTodoDAO.getTodos();

        assertThat("getting todos when two Todo are created returns a list with these two Todos", currentTodos, hasItems(newTodo0, newTodo1));
    }

    @Test
    public void getTodoWithAnExistingTodo() {
        long id = 0L;
        Todo newTodo = TodoBuilder.aTodo(0L, "title").withContent(Optional.of("content")).build();
        Optional<Todo> expectedTodo = Optional.of(newTodo);

        dummyTodoDAO.createTodo(newTodo);
        Optional<Todo> currentTodo = dummyTodoDAO.getTodo(id);

        assertThat("getting an existing todo returns the present todo", currentTodo, is(expectedTodo));
    }

    @Test
    public void getTodoWithANonExistingTodo() {
        long id = 0L;
        Optional<Todo> expectedTodo = Optional.absent();

        Optional<Todo> currentTodo = dummyTodoDAO.getTodo(id);

        assertThat("getting a non existing todo returns an absent todo", currentTodo, is(expectedTodo));
    }
}
