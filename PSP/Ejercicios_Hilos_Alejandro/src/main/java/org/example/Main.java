import org.example.ContadorRunnable;
import org.example.ContadorThread;

void main(String[] args) {

//    ContadorThread t1 = new ContadorThread();
//    ContadorThread t2 = new ContadorThread();
//    t1.start();
//    t2.start();


    Thread r1 = new Thread(new ContadorRunnable());
    Thread r2 = new Thread(new ContadorRunnable());
    r1.start();
    r2.start();
}
