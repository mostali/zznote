package zznote;

import com.j256.ormlite.logger.Level;
import lombok.SneakyThrows;
import mpc.console.QuestAnswerAsync;
import mpc.env.AP;
import mpc.env.APP;
import mpc.env.Env;
import mpc.env.boot.AppBoot;
import mpu.IT;
import mpu.Sys;
import mpu.X;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import zk_notes.AppNotesBoot;
import zk_notes.AppNotesProps;
import zk_os.*;
import zznote_nett.AppZnnTBot;
import zznotet.routes.GetNoteTRoute;

import java.io.IOException;


@Controller
@SpringBootApplication
//@EnableAutoConfiguration(exclude = WebSecurityConfig.class)

//@EnableJpaRepositories(basePackages = "com.sweng.giflib.repository", entityManagerFactoryRef = "sessionFactory")

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@SpringBootApplication(exclude = {
//		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
//}
//		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
//)
//@ComponentScan
//https://stackoverflow.com/questions/28664064/spring-boot-not-a-managed-type
//@Profile("dev")

@ComponentScan(basePackages = {"zznote", "zk_core", "zk_os", "utl_spring", "utl_rest"})
@EntityScan(basePackages = {"zk_os"})
//@EnableJpaRepositories("zk_os.db")
public class ZznApplication extends AppNotesBoot implements ApplicationRunner {
	@Bean
	public AppZosFilter webAppRsrcFilter() {
		return new AppZosFilter();
	}

	@Bean
	public Zn_AppAuthFilter webAppAuthFilter() {
		return new Zn_AppAuthFilter();
	}

//	@Bean
//	public WebAppFilter webAppFilter() {
//		return new WebAppFilter();
//	}


	public static class AppZznBoot {
		public static void main(String[] args) {
//			AbstractEntityManagerFactoryBean.OBJECT_TYPE_ATTRIBUTE;
		}
	}

	public static class Run_LOCAL {
		public static void main(String[] args) throws Exception {
			System.setProperty("spring.devtools.restart.enabled", "false");//otherwise show error on start https://stackoverflow.com/questions/32770884/breakpoint-at-throw-new-silentexitexception-in-eclipse-spring-boot
			ZznApplication.main(args);
		}
	}

	public static class Run_PROD_LOCAL {
		public static void main(String[] args) throws Exception {
			System.setProperty("spring.devtools.restart.enabled", "false");//otherwise show error on start https://stackoverflow.com/questions/32770884/breakpoint-at-throw-new-silentexitexception-in-eclipse-spring-boot
			System.setProperty("spring.profiles.active", "prod-local");
			ZznApplication.main(args);
		}
	}

	@SneakyThrows
	public static void main() {
		main(new String[0]);
	}

	private static class Usr12Test {
		public static void main(String[] args) {
			ZznApplication.main();

			Sys.exit("Go");
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			main_(args);
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			if (L.isErrorEnabled()) {
				L.error("main", t);
			}
		}
	}

	public static void main_(String[] args) throws Exception {

		boot_app(AppZznBoot.class, args);

		SpringApplication app = new SpringApplication(ZznApplication.class);
//		SpringApplication.run(ZznApplication.class);
		app.run(args);

		Sys.p("Running SBA App FINISH:" + APP.CORE.getPort_ReturnIf80());

		com.j256.ormlite.logger.Logger.setGlobalLogLevel(Level.TRACE);

		if (!Env.isTodoDateEnd()) {
			DevelopmentConfig.init();
		}

		//application.addListeners(new WebServerPortFileWriter());
		//application.addListeners(new ApplicationPidFileWriter());

	}

	@Autowired
	private Environment environment;

	/**
	 * *
	 * >>>>>>>>>>>>>>>>>>>>>>> STEP 1 >>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * *
	 */

	@Bean
	@SneakyThrows
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) { //1

		//args is analog Source Args
		//U.p("Ai Environment:::Sources args:::" + Arrays.asList(AiApp.get().applicationArguments.getSourceArgs()));
		//U.p("Ai Environment:::Option names:::" + AiApp.get().applicationArguments.getOptionNames());
		//U.p("Ai Environment:::NonOption args:::" + AiApp.get().applicationArguments.getNonOptionArgs());

		/**
		 * *************************************************************
		 * ---------------------------- INIT ENV -----------------------
		 * *************************************************************
		 */


		{
			//init app profile -need for getting property in dump's
			AP.reinitCache(environment.getActiveProfiles());
			AppZosCore.initEnv(environment);
		}

		IT.isDirExist(Env.RPA);


		/**
		 * *************************************************************
		 * ---------------------------- INIT APP -----------------------
		 * *************************************************************
		 */

		initAppFinderPSP("zznote_pages");

		//

		boolean hasInet = true;

		initTrm("zznote");

		ZznNetApp.get();

		if (hasInet && AppNotesProps.APR_BOT_TG_ENABLE.getValueOrDefault()) {

			try {
				String[] xnote_packs = new String[]{//
						GetNoteTRoute.class.getPackage().getName(),//
						AppZnnTBot.class.getPackage().getName() + ".routes"//
				};

				AppZnnTBot.boot_app(xnote_packs);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				if (!QuestAnswerAsync.CONTINUE_OR_ABORT(6000, "Error running app tbot, continue?")) {
					X.throwException(ex);
				}
			}
		} else {
			AppBoot.L.info("TgBot off {}", AppNotesProps.APR_BOT_TG_ENABLE);
		}


		if (hasInet && AppNotesProps.APR_BOT_VK_ENABLE.getValueOrDefault()) {
			AuthZnsiLPS.runAsDefaultLPS();
		} else {
			AppBoot.L.info("VkBot off {}", AppNotesProps.APR_BOT_VK_ENABLE);
		}

		AppZzn.runned();

		return args ->

				//			System.out.println("FirstRun:::" + AutoContext.getBean(WebUsrService.BEANNAME, WebUsrService.class).initDB()
				System.out.println("App CmdLine Args inited");
	}

	/**
	 * *
	 * >>>>>>>>>>>>>>>>>>>>>>> STEP 2 >>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * *
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {//2


	}

	/**
	 * *
	 * >>>>>>>>>>>>>>>>>>>>>>> STEP 3 >>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * *
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws IOException {//3
		boot_step3(getClass());
	}


//	@GetMapping("/mvvm")
//	public String mvvm() {
//		return "mvvm";
//	}

//	@GetMapping("/app")
//	public String app() {
//		return "app";
//	}

	@GetMapping("/fm")
	public String fm() {
		return "fm";
	}

//	@GetMapping("/test")
//	public String test() {
//		return "test";
//	}

//	@GetMapping("/resources")
//	public String resourcesExample() {
//		return "resources";
//	}

//	@GetMapping("/fscreen")
//	public String fscreen() {
//		return "fscreen ";
//	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}

