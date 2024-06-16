package next.dao;

import next.model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockQuestionDao implements QuestionDao {
    private Map<Long, Question> questions = new HashMap<>();

    @Override
    public Question insert(Question question) {
        return questions.put(question.getQuestionId(), question);
    }

    @Override
    public List<Question> findAll() {
        return List.copyOf(questions.values());
    }

    @Override
    public Question findById(long questionId) {
        return questions.get(questionId);
    }

    @Override
    public void update(Question question) {
        findById(question.getQuestionId()).update(question);
    }

    @Override
    public void delete(long questionId) {
        questions.remove(questionId);
    }

    @Override
    public void updateCountOfAnswer(long questionId) {
    }
}
