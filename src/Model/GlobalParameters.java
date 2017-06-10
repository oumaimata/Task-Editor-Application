package Model;

import java.util.Enumeration;

/**
 * Created by Théo on 17/05/2017.
 */
public class GlobalParameters {
    public enum TypePrecondition {
        NOMOLOGIQUE,
        REGLEMENTAIRE,
        CONTEXTUELLE,
        FAVORABLE
    }

    public enum TypeConditionArret {
        satisfaction("satisfaction"),
        assertion("assertion"),
        duree("duree"),
        iteration("iteration"),
        instance("instance");

        private String name;

        TypeConditionArret(String name) {this.name = name;}

        public String getName() {return name;}
    }

    public enum TypeCondition {
        nomological("nomological"), // TODO change the XML
        precondition("precondition"),
        satisfaction("satisfaction"),
        arret("arret");

        private String name;

        TypeCondition(String name) {this.name = name;}

        public String getName() {return name;}
    }

    public enum TypeAssertion {
        activity("activity"),
        world("world");

        private String name;

        TypeAssertion(String name) {this.name = name;}

        public String getName() {return name;}
    }

    public enum OperateurLogique {
        AND("AND"),
        OR("OR"),
        XOR("XOR"),
        NOT("NOT");

        private String name;

        OperateurLogique(String name) {this.name = name;}

        public String getName() {return name;}
    }

    public enum Nature {
        INTERRUPTIBLE("interruptible"),
        OPTIONELLE("optional"),
        ITERATIVE("iterative");

        private String name;

        Nature(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum RelationAllen{
        // X is the left part of the assertion and Y the right part

        before("<"), // X takes place before Y
        after(">"), // X takes place after Y
        meet("m"), // X meets Y, X est directement suivi par Y
        meetInverse("mi"), // Y meets X, Y est directement suivi par X
        overlap("o"), // X overlaps with Y
        overlapInverse("oi"), // Y overlaps with X
        start("s"), // X start at the same time as Y
        startInverse("si"), // Y start at the same time as X
        during("d"), // X happens during Y
        duringInverse("di"), // Y happens during X
        finish("f"), // X finishes at the same time as Y
        finishInverse("fi"), // Y finishes at the same time as X
        equal("="); // X happens at the same time as Y

        private String name;
        static public RelationAllen fromString(String symbol){
            if (symbol.equals(after.getName())){return after;}
            if (symbol.equals(before.getName())){return before;}
            if (symbol.equals(meet.getName())){return meet;}
            if (symbol.equals(meetInverse.getName())){return meetInverse;}
            if (symbol.equals(overlap.getName())){return overlap;}
            if (symbol.equals(overlapInverse.getName())){return overlapInverse;}
            if (symbol.equals(start.getName())){return start;}
            if (symbol.equals(startInverse.getName())){return startInverse;}
            if (symbol.equals(during.getName())){return during;}
            if (symbol.equals(duringInverse.getName())){return duringInverse;}
            if (symbol.equals(finish.getName())){return finish;}
            if (symbol.equals(finishInverse.getName())){return finishInverse;}
            if (symbol.equals(equal.getName())){return equal;}
            return null;
        }

        RelationAllen(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum TypeConstructeur{
        IND("IND"),
        SEQ("SEQ"),
        SEQ_ORD("SEQ_ORD"),
        PAR("PAR"),
        PAR_SIM("PAR_SIM"),
        PAR_START("PAR_START"),
        PAR_END("PAR_END");

        private String name;

        TypeConstructeur(String name) {this.name = name;}

        public String getName() {return name;}
    }

}
