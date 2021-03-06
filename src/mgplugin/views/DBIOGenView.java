package mgplugin.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import mgplugin.Activator;
import mgplugin.generator.SourceGenerator;
import mgplugin.generator.entity.SourceTemplate;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * <pre>
 * @programName : DBIOGenView
 * @description : 프레임워크 SQL & VO 생성 뷰어
 * @history
 * ----------   ---------------   ------------------------------------------------------------------
 * 수정일       수정자            수정내용
 * ----------   ---------------   ------------------------------------------------------------------
 * 2019.12.02   김도진         최초생성
 *
 * </pre>
 */
public class DBIOGenView extends ViewPart {

    public static final String ID = "mgplugin.views.DBIOGenView"; //$NON-NLS-1$
    private Text textDBIO;
    private Label lblConnStatus;
    public static Text textParameter;
    public static Text textResult;
    
    public DBIOGenView() {
    }
    
    /**
     * Create contents of the view part.
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        
        parent.setLayout(new FormLayout());
        textDBIO = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        FormData fd_textDBIO = new FormData();
        fd_textDBIO.right = new FormAttachment(0, 196);
        fd_textDBIO.left = new FormAttachment(0, 10);
        textDBIO.setLayoutData(fd_textDBIO);
        
        Button btnCreateDBIO = new Button(parent, SWT.NONE);
        btnCreateDBIO.setVisible(false);
        btnCreateDBIO.addSelectionListener(new SelectionAdapter() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if ( connectionDb(parent) == false ) return;
                
                // 테이블 목록
                String value = textDBIO.getText().trim();
                if (StringUtils.isEmpty(value)) {
                    MessageDialog.openInformation(parent.getShell(), "확인", "대상이 없습니다.");
                    return;
                }
                
                List<String> tableList = Arrays.asList(value.split("\n"));
                int tableCnt = tableList.size();
                boolean result = MessageDialog.openQuestion(parent.getShell(),"기본DBIO 생성", tableCnt+ "건 생성[tis.biz..dbio테이블] 하시겠습니까?\n\n파일 존재하는 경우 덮어쓰게 됩니다.");
                if (result) {
                    
                    List<SourceTemplate> resultVoList     = new ArrayList<>();
                    List<SourceTemplate> resultMapperList = new ArrayList<>();
                    
                    // 생성
                    SourceGenerator.createDefaultDBIO(tableList, resultVoList, resultMapperList);
                    
                    /**
                     * DBIO VO.java 생성
                     */
                    for (SourceTemplate sourceTemplate : resultVoList) {
                        String srcPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();  // WORKSPACE PATH
                        
                        srcPath       += "\\" + Activator.getProperty("project.name"      );                                 // 프로젝트명
                        srcPath       += "\\" + Activator.getProperty("project.sourcePath");
                        srcPath       += "\\" + sourceTemplate.getPackageName().replace(".", "\\");
                        
                        // 디렉토리 없는 경우 생성
                        File targetDir = new File(srcPath);
                        if (!targetDir.exists()) {
                            targetDir.mkdirs();
                        }
                        
                        // 대상파일
                        File targetFile = new File(srcPath, sourceTemplate.getTypeName() + ".java");
                        
                        try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile.getPath()), "UTF8"))) {
                            output.write(sourceTemplate.getSource());
                            Activator.console(targetFile.getAbsolutePath() + " 생성...");
                        } catch (Exception e1) {
                            Activator.console(e1.toString());
                            e1.printStackTrace();
                        }
                    }
                    
                    /**
                     * Mapper.xml & Mapper.java 생성
                     */
                    for (SourceTemplate sourceTemplate : resultMapperList) {
                        // tis.xxx.xxx -> mybatis\xxx\xxx
                        //String mapperPath = sourceTemplate.getPackageName().replace(Activator.getProperty("project.rootPackage") + ".", "mybatis.").replace(".", "\\");
                        
                        String srcPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();  // WORKSPACE PATH
                        srcPath       += "\\" + Activator.getProperty("project.name"        );                               // 프로젝트명
                        srcPath       += "\\" + Activator.getProperty("project.resourcePath");
                        srcPath       += "\\" + sourceTemplate.getPackageName();                                             // mybatis/xxx/xxx
                        
                        // 디렉토리 없는 경우 생성
                        File targetDir = new File(srcPath);
                        if (!targetDir.exists()) {
                            targetDir.mkdirs();
                        }
                        
                        // 대상파일
                        File targetFile = new File(srcPath, sourceTemplate.getTypeName() + "Mapper.xml");
                        
                        try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile.getPath()), "UTF8"))) {
                            output.write(sourceTemplate.getSource());
                            
                            Activator.console(targetFile.getAbsolutePath() + " 생성...");
                        } catch (Exception e1) {
                            Activator.console(e1.toString());
                            e1.printStackTrace();
                        }
                        
                        /**
                         * Mapper.xml 읽어서 Mapper.java 생성
                         */
                        try {
                            SourceTemplate interfaceSourceTemplate = SourceGenerator.mapperToInterface(targetFile.getAbsolutePath());
                            srcPath  = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath();  // WORKSPACE PATH
                            srcPath += "\\" + Activator.getProperty("project.name"      );                                 // 프로젝트명
                            srcPath += "\\" + Activator.getProperty("project.sourcePath");
                            srcPath += "\\" + interfaceSourceTemplate.getPackageName().replace(".", "\\");
                            
                            // 디렉토리 없는 경우 생성
                            targetDir = new File(srcPath);
                            if (!targetDir.exists()) {
                                targetDir.mkdirs();
                            }
                            
                            // 대상파일
                            targetFile = new File(srcPath, interfaceSourceTemplate.getTypeName() + ".java");
                            
                            try (BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile.getPath()), "UTF8"))) {
                                output.write(interfaceSourceTemplate.getSource());
                                Activator.console(targetFile.getAbsolutePath() + " 생성...");
                            } catch (Exception e1) {
                                Activator.console(e1.toString());
                                e1.printStackTrace();
                            }
                        } catch (Exception e1) {
                            Activator.console(e1.toString());
                            e1.printStackTrace();
                        }
                    }
                    
                    MessageDialog.openInformation(parent.getShell(), "확인", "처리내용 console 확인 후 폴더 새로고침 하세요.");
                }
            }
        });
        
        FormData fd_btnCreateDBIO = new FormData();
        fd_btnCreateDBIO.bottom = new FormAttachment(100, -10);
        btnCreateDBIO.setLayoutData(fd_btnCreateDBIO);
        btnCreateDBIO.setText("기본 DBIO파일생성");
        
        lblConnStatus = new Label(parent, SWT.NONE);
        lblConnStatus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if ( connectionDb(parent) == false ) return;
            }
        });
        fd_textDBIO.top = new FormAttachment(lblConnStatus, 6);
        FormData fd_lblConnStatus = new FormData();
        lblConnStatus.setLayoutData(fd_lblConnStatus);
        lblConnStatus.setText("DB...");
        
        Label label = new Label(parent, SWT.NONE);
        FormData fd_label = new FormData();
        fd_label.top = new FormAttachment(0, 31);
        fd_label.left = new FormAttachment(0, 10);
        label.setLayoutData(fd_label);
        
        Composite composite = new Composite(parent, SWT.NONE);
        fd_btnCreateDBIO.top = new FormAttachment(composite, 6);
        fd_btnCreateDBIO.right = new FormAttachment(composite, 0, SWT.RIGHT);
        FillLayout fl_composite = new FillLayout(SWT.HORIZONTAL);
        fl_composite.spacing = 10;
        composite.setLayout(fl_composite);
        FormData fd_composite = new FormData();
        fd_composite.bottom = new FormAttachment(textDBIO, 0, SWT.BOTTOM);
        fd_composite.left = new FormAttachment(textDBIO, 6);
        fd_composite.top = new FormAttachment(0, 31);
        composite.setLayoutData(fd_composite);
        
        textParameter = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        
        textResult = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        
        Button btnNewButton = new Button(parent, SWT.NONE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                textParameter.setText("");
                textResult.setText("");
            }
        });
        FormData fd_btnNewButton = new FormData();
        fd_btnNewButton.top = new FormAttachment(btnCreateDBIO, 0, SWT.TOP);
        fd_btnNewButton.left = new FormAttachment(composite, 0, SWT.LEFT);
        btnNewButton.setLayoutData(fd_btnNewButton);
        btnNewButton.setText("지우기");
        
        Button btnNewButton_1 = new Button(parent, SWT.NONE);
        fd_composite.right = new FormAttachment(btnNewButton_1, 0, SWT.RIGHT);
        fd_lblConnStatus.top = new FormAttachment(btnNewButton_1, 6, SWT.TOP);
        btnNewButton_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Activator.getDefault().showConsole(Activator.MG_PLUGIN_CONSOLE);
            }
        });
        btnNewButton_1.setImage(ResourceManager.getPluginImage("org.eclipse.ui.console", "/icons/full/eview16/console_view.png"));
        FormData fd_btnNewButton_1 = new FormData();
        fd_btnNewButton_1.top = new FormAttachment(0, 4);
        fd_btnNewButton_1.right = new FormAttachment(100, -10);
        btnNewButton_1.setLayoutData(fd_btnNewButton_1);
        btnNewButton_1.setText("콘솔");
        
        Label lblVersion = new Label(parent, SWT.NONE);
        fd_lblConnStatus.left = new FormAttachment(lblVersion, 22);
        FormData fd_lblVersion = new FormData();
        fd_lblVersion.bottom = new FormAttachment(textDBIO, -6);
        fd_lblVersion.left = new FormAttachment(0, 10);
        lblVersion.setLayoutData(fd_lblVersion);
        lblVersion.setText(Activator.VERSION);
        
        Button btnQuery = new Button(parent, SWT.NONE);
        fd_textDBIO.bottom = new FormAttachment(100, -41);
        btnQuery.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if ( connectionDb(parent) == false ) return;
                
                // 테이블 목록
                String value = textDBIO.getText().trim();
                if (StringUtils.isEmpty(value)) {
                    MessageDialog.openInformation(parent.getShell(), "확인", "대상이 없습니다.");
                    return;
                }
                
                List<String> tableList = Arrays.asList(value.split("\n"));
                List<String> resultList = SourceGenerator.createDefaultQuery(tableList);
                
                textParameter.setText(String.join("\n", resultList));
            }
        });
        
        FormData fd_btnQuery = new FormData();
        fd_btnQuery.top = new FormAttachment(textDBIO, 6);
        fd_btnQuery.left = new FormAttachment(textDBIO, 0, SWT.LEFT);
        btnQuery.setLayoutData(fd_btnQuery);
        btnQuery.setText("기본쿼리출력");
        
        Button btnVo = new Button(parent, SWT.NONE);
        btnVo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if ( connectionDb(parent) == false ) return;
                
                // 테이블 목록
                String value = textDBIO.getText().trim();
                if (StringUtils.isEmpty(value)) {
                    MessageDialog.openInformation(parent.getShell(), "확인", "대상이 없습니다.");
                    return;
                }
                
                List<String> tableList = Arrays.asList(value.split("\n"));
                List<String> resultList = SourceGenerator.createDefaultVo(tableList);
                
                textParameter.setText(String.join("\n", resultList));
            }
        });
        FormData fd_btnVo = new FormData();
        fd_btnVo.top = new FormAttachment(textDBIO, 6);
        fd_btnVo.left = new FormAttachment(btnQuery, 6);
        btnVo.setLayoutData(fd_btnVo);
        btnVo.setText("기본VO");
        
        Button btnExtractBody = new Button(parent, SWT.NONE);
        btnExtractBody.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String regx = "public class \\w* extends BaseObject<\\w*>";
                if ( Pattern.compile(regx).matcher(textParameter.getText()).find() ) {
                    int paramStart = textParameter.getText().indexOf("{") + 1;
                    int paramEnd   = textParameter.getText().lastIndexOf("}") - 1;
                    textParameter.setText("    " +textParameter.getText(paramStart , paramEnd).trim());
                }
                
                if ( Pattern.compile(regx).matcher(textResult.getText()).find() ) {
                    int resultStart = textResult.getText().indexOf("{") + 1;
                    int resultEnd   = textResult.getText().lastIndexOf("}") - 1;
                    textResult.setText("    " + textResult.getText(resultStart, resultEnd).trim());
                }
            }
        });
        FormData fd_btnExtractBody = new FormData();
        fd_btnExtractBody.bottom = new FormAttachment(btnCreateDBIO, 0, SWT.BOTTOM);
        fd_btnExtractBody.left = new FormAttachment(btnNewButton, 6);
        btnExtractBody.setLayoutData(fd_btnExtractBody);
        btnExtractBody.setText("내용추출{...}");
       
        createActions();
        initializeToolBar();
        initializeMenu();
    }
    
    
    /**
     * Create the actions.
     */
    private void createActions() {
        if ( Activator.getConnection() == null) {
            lblConnStatus.setText("DB 접속실패, 재 접속 하려면 더블클릭하세요.                                                 ");
        } else {
            lblConnStatus.setText("DB " + Activator.getConnection().toString());
        }
    }
    
    /**
     * Create the actions.
     */
    private boolean connectionDb(Composite parent) {
        Connection dbConn = Activator.getConnection();
        if ( dbConn == null) {
            MessageDialog.openWarning(parent.getShell(), "확인", "DB 연결정보가 없습니다.");
            lblConnStatus.setText("DB 접속실패, 재 접속 하려면 더블클릭하세요.                                                 ");
            return false;
        } else {
            lblConnStatus.setText("DB " + dbConn.toString());
        }
        return true;
    }    
    
    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        @SuppressWarnings("unused")
        IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
    }
    
    /**
     * Initialize the menu.
     */
    private void initializeMenu() {
        @SuppressWarnings("unused")
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
    }

    @Override
    public void setFocus() {
        // Set the focus
    }
    
    public static void main(String[] args) throws Exception {
        
//        try {
//            Activator.CONNECTION = DriverManager.getConnection("jdbc:sqlserver://192.168.0.103:1433;databaseName=ERPFRM;user=sa;password=wjdtnsdyd~1");
//        }
//        catch (SQLException e) {
//            System.err.println("DB Connection error...");
//            e.printStackTrace();
//        }
        
        Activator.initThisPlugin("C:\\eclipse_rcp\\runtime-EclipseApplication\\.metadata\\mgplugin");
        
        List<String> tableList = new ArrayList<>();
        tableList.add("TB_XX002M");
        
        List<SourceTemplate> resultVoList        = new ArrayList<>();
        List<SourceTemplate> resultMapperList    = new ArrayList<>();
        
        SourceGenerator.createDefaultDBIO(tableList, resultVoList, resultMapperList);
        
        System.out.println(resultVoList.get(0).getSource());
        System.out.println("==================================");
        System.out.println("==================================");
        
        Activator.closeThisPlugin();
        
        
    }
}
