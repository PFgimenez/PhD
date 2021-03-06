library(ggplot2)

#--------------------------------------------------------------------------------------------
#Parametres globaux
dataset_name = "champi"
taille_img_x = 1024/2
taille_img_y = 720/2
#fin parametres globaux
#--------------------------------------------------------------------------------------------

#--------------------------------------------------------------------------------------------
#data
#result
Naif = c(0.6878322504430006, 0.7526580035440047, 0.7941893089190786, 0.8277835203780272, 0.8470171293561725, 0.8625590667454224, 0.8761444181925576, 0.8808697578263438, 0.8924616066154755, 0.8967808623744832, 0.9006940342587123, 0.9083727111636148, 0.9097386296515062, 0.9083727111636148, 0.912322799763733, 0.9146485528647371, 0.9214412285883048, 0.9216627288836385, 0.9231024808033077, 0.9257235676314235, 0.9273479031305375, 0.9287507383343178, 0.9312241582988777, 0.931814825753101, 0.9340667454223273, 0.9353588304784406, 0.9365770821027761, 0.9389028352037803, 0.9375, 0.9397519196692262, 0.9406010041346722, 0.9424468399291199, 0.9441450088600118, 0.9435174246898996, 0.9455109273479031, 0.9453632604843473, 0.9486857649143532, 0.948058180744241, 0.9498301831069108, 0.9525989367985824, 0.9511591848789132, 0.952931187241583, 0.9543709391612523, 0.9520451860602481, 0.9551092734790313, 0.9554415239220319, 0.956179858239811, 0.9568074424099232)
vpop = c(0.6878322504430006, 0.7516981689308919, 0.7957028942705257, 0.8281157708210277, 0.8466479621972829, 0.8626329001772003, 0.8754060838747785, 0.8811281748375664, 0.8917971057294743, 0.8957102776137035, 0.9009524512699351, 0.9080404607206143, 0.90996012994684, 0.9083727111636148, 0.9126550502067337, 0.9152023036030714, 0.9203706438275251, 0.9213673951565269, 0.9216258121677495, 0.9257974010632014, 0.9275694034258712, 0.9295259893679858, 0.9311134081512109, 0.9316671588895452, 0.9349158298877732, 0.9348050797401063, 0.9365401653868872, 0.9389766686355582, 0.9372046662728883, 0.9397519196692262, 0.9400472533963379, 0.9434805079740106, 0.9438865918487891, 0.9443295924394566, 0.9462861783815711, 0.9463230950974602, 0.9485750147666864, 0.9485011813349085, 0.9494240992321323, 0.9530419373892498, 0.9507900177200236, 0.9531896042528056, 0.9540386887182516, 0.9520082693443591, 0.9551831069108092, 0.9556630242173656, 0.9560691080921441, 0.9564382752510336)
oracle = c(0.6880906674542233, 0.751587418783225, 0.7948168930891908, 0.828780271707029, 0.8484568812758417, 0.8648479031305375, 0.8761444181925576, 0.8821249261665682, 0.89441819255759, 0.8991435321913762, 0.90501329001772, 0.9122120496160662, 0.9154238038984052, 0.9150915534554046, 0.9191523922031896, 0.9221426461901949, 0.9265357353809806, 0.9270894861193149, 0.9311872415829888, 0.9346574128765505, 0.9360233313644418, 0.939124335499114, 0.9422253396337862, 0.9433697578263438, 0.9461754282339043, 0.9467291789722386, 0.9499040165386887, 0.9518975191966923, 0.9535587714116952, 0.9542601890135853, 0.9556630242173656, 0.9596869462492617, 0.9598346131128175, 0.9614220318960425, 0.9624187832250443, 0.9640800354400473, 0.9665165386887182, 0.9686577082102776, 0.9703927938570585, 0.9705035440047254, 0.9703927938570585, 0.9733830478440638, 0.9744905493207324, 0.9729400472533963, 0.9778868871825163, 0.9785144713526285, 0.9787728883638511, 0.9799173065564087)

#result conf
c_Naif = c(0.010788039810114286,0.010613967930554224,0.010327822508316438,0.010081537223655703,0.00982794580383695,0.009651264595099317,0.00957626007098271,0.009399428271861889,0.009489571296648869,0.009276420862719272,0.009258340147205195,0.009305730614958785,0.009096726141523038,0.008985802323340075,0.009073831958006024,0.009121168582342007,0.009080896682197989,0.009087943291044872,0.009029264347801646,0.00896749505397351,0.009112464369433778,0.009018461410494913)
c_vpop = c(0.010861588073384765,0.0107445767718828,0.01038439316283025,0.009979573154871031,0.00957626007098271,0.009317036614863756,0.009048962151439131,0.008961979979620139,0.009079132201113626,0.008923076528085796,0.008939813476321676,0.008934245200211658,0.009115949411549001,0.008872283506817092,0.009093216384823334,0.00913503725760684,0.009157422182526739,0.009308966152977684,0.009465471151164086,0.009477553093394446,0.009746276324482443,0.00983932143544584)

#temps
Naiftemps = c(0.056763171551978735, 0.055890602000886004, 0.06165468391907856, 0.06334213518901359, 0.06319974409332546, 0.06335727451269935, 0.06250468465741288, 0.06118589903278204, 0.06011799143532191, 0.059176268901358534, 0.05806812016391022, 0.05635740516095688, 0.05512187363408151, 0.05392697836680449, 0.052249952008269346, 0.05137013496751329, 0.05028583649586533, 0.04814491165829888, 0.04788100727259303, 0.045658303123154165, 0.0444531670481394, 0.04321111403573538, 0.04212479603514471, 0.040809936946249264, 0.03980495780419374, 0.03868906246308328, 0.037504149106615474, 0.036458728920555225, 0.035950678824571765, 0.03468500217808624, 0.03365401458210278, 0.03284524320732428, 0.03196224878174837, 0.0322784028721205, 0.03030849811724749, 0.029605394455109273, 0.02890349918783225, 0.028284020267277023, 0.02761261924099232, 0.026857414980803306, 0.026226878174837567, 0.025591837049616067, 0.024955184583579443, 0.024798421293561725, 0.023783111636148848, 0.023747315231836975, 0.022541211348198465, 0.021836834243945658)
vpoptemps = c(0.008608962935617248, 0.5421996886075015, 5.736728327192853, 9.598066125812167, 30.653669532560542, 19.45889230485824, 17.09588862961459, 15.125646240733904, 13.257270470429711, 11.917461505205257, 10.454172979178972, 9.461963145599528, 8.355109322578263, 7.592733656453042, 6.87333264511961, 6.225347725893385, 5.4397985721721795, 4.979618078854105, 4.424300127805671, 3.9696491067631423, 3.5824246680818073, 3.2306991265135854, 2.926117568074424, 2.6631194507161844, 2.2593035889692854, 2.1337227951491435, 1.8618118125369167, 1.669923456844359, 1.4636099874114, 1.2836704170850561, 1.1540355015505022, 1.0166425427864738, 0.8517278377510337, 0.7512722990253987, 0.6607060973124631, 0.5833630065342588, 0.4976690343325458, 0.4303620654533373, 0.3549467602259303, 0.3047600555227407, 0.26277731098641466, 0.21574819495717662, 0.17104305404607206, 0.1440633185543414, 0.1169600950236267, 0.09338258287802717, 0.07320619691376255, 0.05753750247341997)

#temps conf
c_Naiftemps = c(5.825714380863392E-5,7.467741713131934E-5,7.874005730354485E-5,9.634253464465466E-5,1.0492256731643185E-4,1.2314165340509745E-4,1.3206651870520007E-4,1.7646944136045804E-4,1.7421049529386254E-4,1.7259208701506554E-4,1.8753422740190828E-4,2.165709993540696E-4,2.392953541891117E-4,2.4272466653793426E-4,2.5865001666338093E-4,2.574419679224536E-4,2.5728239038577303E-4,3.054320190361853E-4,3.102239821327525E-4,3.354078769230858E-4,3.213923298565306E-4,3.256574310299249E-4)
c_vpoptemps = c(6.98017195027686E-4,0.0013948425795824164,0.0014257605092347248,5.747709647325909E-4,4.217789739549773E-4,4.5748807978158166E-4,4.868895926868611E-4,5.34863288491936E-4,5.89507750837444E-4,6.400229646381698E-4,6.818146563558449E-4,7.195175587107395E-4,0.0010305845860537378,0.0010825020708166793,0.0011391206729347194,0.0011967570863145396,0.0012433720266704928,0.0012291128279559114,0.0012167330383921018,9.729224254311251E-4,8.616359596943645E-4,7.577140443470682E-4)
#fin data
#--------------------------------------------------------------------------------------------


#--------------------------------------------------------------------------------------------
#parametres graphes
# -- manuels
x_pas_erreur = 4
y_pas_erreur = 3
x_legend_erreur = 0
y_legend_erreur = 50
y_padding_erreur = 1

x_pas_time = 4
y_pas_time = 3
x_legend_time = 0
y_legend_time = 0.013
y_padding_time = 0.01

# -- auto calcul

#MAJ VERS ERROR
Naif = 100-100*Naif
vpop = 100-100*vpop
oracle = 100-100*oracle
c_Naif = 100*c_Naif
c_vpop = 100*c_vpop
#FIN MAJ VERS ERROR

nb_val = length(Naif)
nb_val = length(vpop)

min_val_erreur = 30000
min_val_erreur = min(min_val_erreur,100-100*Naif)
min_val_erreur = min(min_val_erreur,100-100*vpop)
min_val_erreur = ceiling(min_val_erreur)

max_val_erreur = 30000
max_val_erreur = max(max_val_erreur,100-100*Naif)
max_val_erreur = max(max_val_erreur,100-100*vpop)
max_val_erreur = floor(max_val_erreur)

size = 0:(nb_val-1)
x_lim_erreur = c(0,(nb_val-1))
if(min_val_erreur-y_padding_erreur <= 0){
  min_val_erreur = min_val_erreur+y_padding_erreur+0.00001
}
y_lim_erreur = c((min_val_erreur-y_padding_erreur),(max_val_erreur+y_padding_erreur))
x_axp_erreur = c(0, (nb_val-1), x_pas_erreur)
y_axp_erreur = c((min_val_erreur-y_padding_erreur),(max_val_erreur+y_padding_erreur),y_pas_erreur)

min_val_time = 30000
min_val_time = min(min_val_erreur,100-100*Naiftemps)
min_val_time = min(min_val_erreur,100-100*vpoptemps)
min_val_time = ceiling(min_val_erreur)

max_val_time = 30000
max_val_time = max(max_val_time,Naiftemps)
max_val_time = max(max_val_time,vpoptemps)
max_val_time = floor(max_val_time)


x_lim_time = c(0,(nb_val-1))
if(min_val_time-y_padding_time <= 0){
  min_val_time = min_val_time+y_padding_time+0.00001
}
size = 0:(nb_val-1)
y_lim_time = c((min_val_time-y_padding_time),(max_val_time+y_padding_time))
x_axp_time = c(0, (nb_val-1), x_pas_erreur)
y_axp_time = c((min_val_time-y_padding_time),(max_val_time+y_padding_time),y_pas_time)
#fin parametres graphes
#--------------------------------------------------------------------------------------------

  png(file=paste("Taux_erreur_sur_",dataset_name,".png",sep=""), bg="white", width=taille_img_x, height=taille_img_y, pointsize = 12)
  (ggplot(NULL, aes(size)) + scale_y_log10(breaks = round(seq(0, 100, by = 5),1)) + annotation_logticks(sides="l") +
    ylab("Taux d'erreur (%)") + xlab("Nombre de variables configurées") + theme_bw() #+ theme(legend.position="bottom")
    +geom_line(aes(y=Naif, colour="Jointree"), colour="turquoise2", linetype = "dotted") + geom_point(aes(y=Naif, shape="Jointree"), colour="turquoise2", fill="turquoise2") 
    +geom_line(aes(y=vpop, colour="DRC"), colour="gold2", linetype = "dotted") + geom_point(aes(y=vpop, shape="DRC"), colour="gold2", fill="gold2")
  +geom_line(aes(y=oracle, colour="Oracle"), colour="black", linetype = "dotted") + geom_point(aes(y=oracle, shape="Oracle"), colour="black", fill="black")
  + scale_colour_manual(name = 'Legend', guide = 'legend',
                      limits = c(NULL
                                  ,'Jointree' #Naif
                                  ,'DRC' #vpop
                                 ,'Oracle'
                      ),
                      values =c(NULL
                                  ,'Jointree'='turquoise2' #Naif
                                  ,'DRC'='gold2' #vpop
                                ,'Oracle'='black'
                                  ))
    + scale_shape_manual(name = 'Legend', guide = 'legend',
                      limits = c(NULL
                                  ,'Jointree' #Naif
                                  ,'DRC' #vpop
                                 ,'Oracle'
                      ),
                      values =c(NULL
                                  ,'Jointree'=24 #Naif
                                  ,'DRC'=2 #vpop
                                ,'Oracle'=1
                                  ))
  
    + guides(shape = guide_legend(override.aes = list(colour = c(NULL
                                  ,'Jointree'='turquoise2' #Naif
                                  ,'DRC'='gold2' #vpop
                                  ,'Oracle'='black'
                                  ),
                                  fill = c(NULL
                                  ,'Jointree'='turquoise2' #Naif
                                  ,'DRC'='gold2' #vpop
                                  ,'Oracle'='black'
                                  ))))
)
  dev.off()

  png(file=paste("Temps_sur_",dataset_name,".png",sep=""), bg="white", width=taille_img_x, height=taille_img_y, pointsize = 12)
  (ggplot(NULL, aes(size))
  + scale_y_log10(
   breaks = scales::trans_breaks("log10", function(x) 10^x),
   labels = scales::trans_format("log10", function(x) round(10^x,2)))
   + annotation_logticks(sides="l")
   + theme_bw() +
    ylab("Temps (ms)") + xlab("Nombre de variables configurées") #+ theme(legend.position="bottom")
    +geom_line(aes(y=Naiftemps, colour="Jointree"), colour="turquoise2", linetype = "dotted") + geom_point(aes(y=Naiftemps, shape="Jointree"), colour="turquoise2", fill="turquoise2") 
    +geom_line(aes(y=vpoptemps, colour="DRC"), colour="gold2", linetype = "dotted") + geom_point(aes(y=vpoptemps, shape="DRC"), colour="gold2", fill="gold2")
   + scale_colour_manual(name = 'Legend', guide = 'legend',
                      limits = c(NULL
                                  ,'Jointree' #Naif
                                  ,'DRC' #vpop
                      ),
                      values =c(NULL
                                  ,'Jointree'='turquoise2' #Naif
                                  ,'DRC'='gold2' #vpop
                                  ))
    + scale_shape_manual(name = 'Legend', guide = 'legend',
                      limits = c(NULL
                                  ,'Jointree' #Naif
                                  ,'DRC' #vpop
                      ),
                      values =c(NULL
                                  ,'Jointree'=24 #Naif
                                  ,'DRC'=2 #vpop
                                  ))
  
    + guides(shape = guide_legend(override.aes = list(colour = c(NULL
                                  ,'Jointree'='turquoise2' #Naif
                                  ,'DRC'='gold2' #vpop
                                  ),
                                  fill = c(NULL
                                  ,'Jointree'='turquoise2' #Naif
                                  ,'DRC'='gold2' #vpop
                                  ))))
  )
  dev.off()
