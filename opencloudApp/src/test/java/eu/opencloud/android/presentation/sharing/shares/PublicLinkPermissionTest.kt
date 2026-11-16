/**
 * openCloud Android client application
 *
 * Copyright (C) 2026 OpenCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.opencloud.android.presentation.sharing.shares

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PublicLinkPermissionTest {
    @Test
    fun `view only grants read access to files and folders`() {
        assertEquals(1, PublicLinkPermission.VIEW.permissions(isFolder = false))
        assertEquals(1, PublicLinkPermission.VIEW.permissions(isFolder = true))
    }

    @Test
    fun `editing a file grants read and update without create delete or reshare`() {
        assertEquals(3, PublicLinkPermission.EDIT.permissions(isFolder = false))
    }

    @Test
    fun `editing a folder grants read update create and delete without reshare`() {
        assertEquals(15, PublicLinkPermission.EDIT.permissions(isFolder = true))
    }

    @Test
    fun `file drop only grants create access`() {
        assertEquals(4, PublicLinkPermission.UPLOAD_ONLY.permissions(isFolder = true))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `file drop cannot be applied to a file`() {
        PublicLinkPermission.UPLOAD_ONLY.permissions(isFolder = false)
    }

    @Test
    fun `web file edit links are restored as editable`() {
        assertEquals(PublicLinkPermission.EDIT, PublicLinkPermission.fromPermissions(3, isFolder = false))
    }

    @Test
    fun `standard folder permissions round trip`() {
        for (option in PublicLinkPermission.values()) {
            assertEquals(option, PublicLinkPermission.fromPermissions(option.permissions(isFolder = true), isFolder = true))
        }
    }

    @Test
    fun `custom combinations are not silently mapped to view only or broader edit access`() {
        for (permissions in listOf(0, 5, 7, 17, 19, 31)) {
            assertNull(PublicLinkPermission.fromPermissions(permissions, isFolder = false))
            assertNull(PublicLinkPermission.fromPermissions(permissions, isFolder = true))
        }
        assertNull(PublicLinkPermission.fromPermissions(4, isFolder = false))
        assertNull(PublicLinkPermission.fromPermissions(15, isFolder = false))
        assertNull(PublicLinkPermission.fromPermissions(3, isFolder = true))
    }
}
