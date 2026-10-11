# AFRICA NICHES Business — projet Android Studio

Application Android conteneur sécurisée (WebView) qui ouvre le back-office web :
**https://business.africaniches.com/**

Éditeur : AFRICA NICHES SARL — Togo  
Contact : info@africaniches.com — https://www.africaniches.com

## Fonctionnement
- Affiche le back-office web existant dans une application Android.
- Laisse l'authentification et les autorisations au site web.
- Active JavaScript et le stockage DOM requis par la plupart des applications web modernes.
- Accepte la navigation HTTPS sur les domaines AFRICA NICHES.
- Ouvre les autres liens HTTPS, `tel:` et `mailto:` dans les applications adaptées.
- Fournit actualisation, barre de progression, message d'erreur réseau et bouton retour Android.
- Refuse le contenu mixte HTTP/HTTPS et désactive l'accès WebView aux fichiers locaux.

## Ouvrir et compiler
1. Décompresser l'archive.
2. Installer Android Studio récent, avec JDK 17 et Android SDK Platform 35.
3. Dans Android Studio, choisir **Open** et sélectionner le dossier `AFRICA_NICHES_ANDROID`.
4. Attendre la synchronisation Gradle. Si Android Studio propose d'installer les composants SDK manquants, accepter.
5. Pour tester : connecter un téléphone Android (débogage USB activé) ou créer un émulateur, puis cliquer **Run**.
6. Pour créer un APK : **Build > Build Bundle(s) / APK(s) > Build APK(s)**. Pour distribuer l'application, générer un APK/AAB signé via **Generate Signed Bundle / APK**.

## Configuration
L'adresse de départ est définie dans `app/src/main/java/com/africaniches/business/MainActivity.kt` dans la constante `homeUrl`.

## Important : portée et prérequis
Ce projet est un client Android léger qui réutilise l'interface web en ligne. Il ne remplace pas le back-office et ne crée pas à lui seul une API. Les modules, données, paiements, tickets, publications du jour, géolocalisation et notifications restent fournis par le site et ses services backend. Le site doit autoriser son usage sur mobile et fonctionner correctement dans Android WebView.

Avant production, tester notamment connexion/déconnexion, cookies de session, formulaires, téléversements d'images, téléchargements PDF, fenêtres de paiement, redirections Mobile Money et compatibilité des cartes. Si le site dépend de fenêtres popup, téléchargements ou flux de paiement qui ne fonctionnent pas dans WebView, une adaptation ciblée de l'application ou une API officielle sera nécessaire.

## Sécurité des API SMS / paiements
Ne jamais intégrer une clé API secrète NGH SMS, un secret Mobile Money ou des identifiants administrateur dans l'application Android. Ces secrets doivent rester côté serveur. Le client Android ne doit appeler que des endpoints backend authentifiés et autorisés.

## Détails techniques
- Kotlin
- Android Gradle Plugin 8.7.3
- Kotlin Android plugin 2.0.21
- compileSdk 35, minSdk 23, Java 17
- Application ID : `com.africaniches.business`
