package com.resync.app.singletons;

import java.util.regex.Pattern;

/* JADX INFO: renamed from: j9.c */
/* JADX INFO: compiled from: PatternSingleton.java */
/* JADX INFO: loaded from: classes.dex */
public final class PatternSingleton {

    private static final Pattern multiredditPattern = Pattern.compile("(http(s)*:)*//((\\w)+\\.)*reddit\\.com(/u(ser)*/([\\.\\+a-zA-Z0-9_-])+)*/m/([\\.\\+a-zA-Z0-9_-])+(\\b|/|\\?)", 2);
    private static final Pattern spritePattern = Pattern.compile("(\\[\\]\\(#sprite)([0-9]{1,10})\\-p([0-9]{1,10})(\\))", 2);
    private static final Pattern xkcdPattern = Pattern.compile("http(s)*://(www\\.)*xkcd.com/([0-9]){1,4}(/)*", 2);
    private static final Pattern userPattern = Pattern.compile("http(s)*://(www\\.|m\\.)*reddit\\.com/u(ser)*/([\\.\\+a-zA-Z0-9_-])+(\\b|/|\\?)", 2);

    private static final Pattern subredditListingPattern = Pattern.compile("(((http(s)|sync)*://(www\\.|m\\.)*reddit\\.com/r/)|r/|/r/)([\\.\\+a-zxA-Z0-9_-])+(/$|/hot(/)*$|/new(/)*$|/rising(/)*$|/top(/)*$|/comments(/)*$|/controversial(/)*$|/guilded(/)*$|$)", 2);

    private static final Pattern duplicatesPattern = Pattern.compile("(http(s)*:)*//((\\w)+\\.)*reddit\\.com/r/([\\.\\+a-zA-Z0-9_-])+(/duplicates)+/([!:%&\\?=\\.\\+/a-zA-Z0-9_-])+(\\b|/|\\?)", 2);
    private static final Pattern commentsPattern = Pattern.compile("(http(s)*:)*//((\\w)+\\.)*reddit\\.com/r/([\\.\\+a-zA-Z0-9_-])+(/comments)*/([!:%&\\?=\\.\\+/a-zA-Z0-9_-])+(\\b|/|\\?)", 2);
    private static final Pattern commentsContextPattern = Pattern.compile("(http(s)*:)*//((\\w)+\\.)*reddit\\.com(/(r|user)/([\\.\\+a-zA-Z0-9_-])+)*/comments/([!:%&\\?=\\.\\+/a-zA-Z0-9_-])+((/\\?context=\\[([0-9])+\\])|(/#([!:%&\\?=\\.\\+/a-zA-Z0-9_-])+))*(\\b|/|\\?)*", 2);

    private static final Pattern subPathPattern = Pattern.compile("/r/([\\.\\+a-zA-Z0-9_-])+/([!:%&\\?=\\.\\+/a-zA-Z0-9_-])+(\\b|/|\\?)", 2);
    private static final Pattern genericRedditPattern = Pattern.compile("http(s)*://((\\w)+\\.)*reddit\\.com/([\\.\\+a-zA-Z0-9_-])+(\\b|/|\\?)", 2);
    private static final Pattern wikiPattern = Pattern.compile("(((http(s)*:)*//((\\w)+\\.)*reddit\\.com)*/r/(\\w)+)*/w(iki)*(/(.*?))*", 2);

    private static final Pattern imgurPattern = Pattern.compile("http(s)*://((\\w)+\\.)*imgur\\.com(\\b|/|\\?)", 2);

    private static final Pattern spoilerTagPattern = Pattern.compile("\\[(s|S)+(p|P)+(o|O)+(i|I)+(l|L)+(e|E)+(r|R)+(s|S)*\\](.*?)", 2);
    private static final Pattern composePattern = Pattern.compile("(http(s)*://((\\w)+\\.)*reddit\\.com)*/message/compose([!:%&\\?=\\.\\+/a-zA-Z0-9_-])*(\\b|/|\\?)", 2);
    private static final Pattern domainPattern = Pattern.compile("http(s)*://((\\w)+\\.)*reddit\\.com/domain/([!:%&\\?=\\.\\+/a-zA-Z0-9_-])*(\\b|/|\\?)", 2);
    private static final Pattern emotePattern = Pattern.compile("\\[\\]\\(/(ajlie|priceless|flutterjerk|flutterroll|twipride|celestiamad|twicrazy|spikemeh|lunateehee|lunawait|derpwizard|abmeh|ajhappy|pinkiefear|twibeam|scootaderp|raritydaww|scootacheer|swagintosh|pinkieawe|ajsup|flutterwhoa|rdcry|silverspoon|ohcomeon|ppcute|abbored|rarityreally|raritynews|sbbook|scootaplease|applegasp|twiright|celestiawut|grannysmith|rarishock|shiningarmor|chrysalis|cadance|applederp|flutterfear|ppboring|rarityyell|fluttershy|ajcower|ajsly|eeyup|rdsmile|fluttersrs|raritydress|takealetter|rdwut|ppshrug|spikenervous|noooo|dj|fluttershh|flutteryay|squintyjack|spikepushy|ajugh|raritywut|dumbfabric|raritywhy|trixiesmug|flutterwink|rarityannoyed|soawesome|ajwut|twisquint|raritywhine|rdcool|abwut|manspike|cockatrice|facehoof|rarityjudge|rarityprimp|twirage|ppseesyou|rdsitting|rdhappy|rdannoyed|trixiesad|twismug|twismile|twistare|changeling|ohhi|party|hahaha|rdscared|flutterblush|gross|derpyhappy|twidaw|ajfrown|hmmm|joy|whattheflut|raritysad|fabulous|derp|cadencesmile|louder|lunasad|derpyshock|shiningpride|pinkamina|loveme|lunagasp|fluttercry|scootaloo|celestia|angel|sneakybelle|allmybits|zecora|photofinish|ppreally|fillytgap|rdhuh|snails|twisad|lyra|bonbon|spitfire|lunamad|cutealoo|happyluna|sotrue|discordsad|wahaha|sbstare|punchdrunk|maud|huhhuh|absmile|dealwithit|pinkiepout|nmm|whooves|rdsalute|twisecret|octavia|colgate|cheerilee|sunsetshimmer|ajbaffle|abhuh|thehorror|sunsetsneaky|twiponder|spikewtf|awwyeah|scootablue|gilda|discentia|macintears|spikehappy|pinkiesad|starlightrage|ajdoubt|flutterhay|diamondtiara|bulkbiceps|spikewhoa|sbwtf|sombra|scootaeww|wasntme|nightmaregrin|sbshocked|discordgrump|twipbbt|spikeapproves|guard|troubleshoes|flimflam|flutternice|abstern|rdsnrk|cocosmile|ppdont|apathia|thcalm|skeptiloo|ajcry|ooh|limestonegrin|rarityeww|raritytired|raritygrump|flutterkay|notangry|goodjob)+\\)", 2);

    public static Pattern getCommentsPattern() {
        return commentsPattern;
    }

    public static Pattern getCommentsContextPattern() {
        return commentsContextPattern;
    }

    public static Pattern getComposePattern() {
        return composePattern;
    }

    public static Pattern getDomainPattern() {
        return domainPattern;
    }

    public static Pattern getDuplicatesPattern() {
        return duplicatesPattern;
    }

    public static Pattern getImgurPattern() {
        return imgurPattern;
    }

    public static Pattern getMultiredditPattern() {
        return multiredditPattern;
    }

    public static Pattern getGenericRedditPattern() {
        return genericRedditPattern;
    }

    public static Pattern getSubPathPattern() {
        return subPathPattern;
    }

    public static Pattern getSubredditListingPattern() {
        return subredditListingPattern;
    }

    public static Pattern getUserPattern() {
        return userPattern;
    }

    public static Pattern getWikiPattern() {
        return wikiPattern;
    }

    public static Pattern getXkcdPattern() {
        return xkcdPattern;
    }
}
