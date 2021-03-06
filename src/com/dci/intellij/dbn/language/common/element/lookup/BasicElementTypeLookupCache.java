package com.dci.intellij.dbn.language.common.element.lookup;

import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.impl.BasicElementType;
import com.dci.intellij.dbn.language.common.element.impl.ElementTypeBase;
import com.dci.intellij.dbn.language.common.element.impl.LeafElementType;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class BasicElementTypeLookupCache extends ElementTypeLookupCacheIndexed<BasicElementType> {
    public BasicElementTypeLookupCache(BasicElementType elementType) {
        super(elementType);
    }

    @Override
    boolean initAsFirstPossibleLeaf(LeafElementType leaf, ElementTypeBase source) {
        return false;
    }

    @Override
    boolean initAsFirstRequiredLeaf(LeafElementType leaf, ElementTypeBase source) {
        return false;
    }

    @Override
    public boolean containsToken(TokenType tokenType) {
        return false;
    }

    @Override
    public boolean startsWithIdentifier() {
        return false;
    }

    @Override
    public boolean checkStartsWithIdentifier() {return false;}

    @Override
    public Set<LeafElementType> collectFirstPossibleLeafs(ElementLookupContext context, @Nullable Set<LeafElementType> bucket) {
        return bucket;
    }

    @Override
    public Set<TokenType> collectFirstPossibleTokens(ElementLookupContext context, @Nullable Set<TokenType> bucket) {
        return bucket;
    }
}