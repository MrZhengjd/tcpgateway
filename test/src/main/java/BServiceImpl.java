import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class BServiceImpl implements BService {
    public AService getaService() {
        return aService;
    }

    public void setaService(AService aService) {
        this.aService = aService;
    }

    @Autowired
    private AService aService;
    @Override
    public void b() {

    }
}
