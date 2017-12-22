package com.fuzzygroup.view2;

//import com.beltser.evolutionproject.mathematics.MathUtils;
//import com.beltser.evolutionproject.mathematics.ThreeVector;

public class Physics {

    public static boolean intersect(VisualRepresentationState entity,
                                    VisualRepresentationState entity2) throws Exception {
//        switch (atom.getShape()) {
//            case CIRCLE:
//            case STAR:
//            case RECT:
//                switch (otherAtom.getFigure()) {
//                    case STAR:
//                    case CIRCLE:
//                    case RECT:
//                        return atom.getPosition().minus(otherAtom.getPosition()).module() <
//                                MathUtils.max(atom.getSize().x, atom.getSize().y) / 2
//                                        + MathUtils.max(otherAtom.getSize().x, otherAtom.getSize().y) / 2;
//                }
//        }
        throw new Exception("Figure type doesn't supported");
    }

    public static boolean intersect(VisualRepresentationState atom, double x, double y) throws Exception {
//        switch (atom.getFigure()) {
//            case CIRCLE:
//            case STAR:
//            case RECT:
//                return atom.getPosition().minus(new ThreeVector(x, y, 0)).module() <
//                        MathUtils.max(atom.getSize().x, atom.getSize().y) / 2 + MathUtils.max(0.01, 0.01) / 2;
//        }
        throw new Exception("Figure type doesn't supported");
    }
}
