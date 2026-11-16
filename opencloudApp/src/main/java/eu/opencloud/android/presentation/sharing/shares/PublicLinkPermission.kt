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

import eu.opencloud.android.lib.resources.shares.RemoteShare

internal enum class PublicLinkPermission {
    VIEW, EDIT, UPLOAD_ONLY;

    fun permissions(isFolder: Boolean): Int = when (this) {
        VIEW -> {
            RemoteShare.READ_PERMISSION_FLAG
        }
        EDIT -> {
            if (isFolder) {
                RemoteShare.READ_PERMISSION_FLAG or RemoteShare.UPDATE_PERMISSION_FLAG or
                    RemoteShare.CREATE_PERMISSION_FLAG or RemoteShare.DELETE_PERMISSION_FLAG
            } else {
                RemoteShare.READ_PERMISSION_FLAG or RemoteShare.UPDATE_PERMISSION_FLAG
            }
        }
        UPLOAD_ONLY -> {
            require(isFolder) { "File drops are only supported for folders" }
            RemoteShare.CREATE_PERMISSION_FLAG
        }
    }

    companion object {
        // Unknown combinations must be preserved rather than silently downgraded to view-only.
        fun fromPermissions(permissions: Int, isFolder: Boolean): PublicLinkPermission? =
            values().firstOrNull { option ->
                (isFolder || option != UPLOAD_ONLY) && option.permissions(isFolder) == permissions
            }
    }
}
