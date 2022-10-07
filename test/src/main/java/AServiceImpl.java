import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class AServiceImpl implements AService {
    public BService getbService() {
        return bService;
    }

    public void setbService(BService bService) {
        this.bService = bService;
    }

    @Autowired
    private BService bService;
    @Override
    public void a() {

    }
}
