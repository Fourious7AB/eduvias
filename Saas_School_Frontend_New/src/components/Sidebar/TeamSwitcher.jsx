import React from 'react'
import {
    SidebarMenu,
    SidebarMenuItem,
} from "@/components/ui/sidebar"
import w from "@/assets/w.png"
import { useSidebar } from '@/components/ui/sidebar'
import schoolLogo from "@/assets/schoolLogo.png"
import schoolLogoWhite1 from "@/assets/schoolLogoWhite1.png"
import { useTheme } from "@/context/ThemeContext";

const TeamSwitcher = () => {
    const { state } = useSidebar();
    const { theme } = useTheme();
    const version = import.meta.env.VITE_REACT_VERSION;

    return (
        <SidebarMenu>
            <SidebarMenuItem>
                {/* for logo  */}
                <div
                    className={`flex justify-center items-center pt-2 ${
                        state === 'collapsed'
                            ? 'border-none pb-0'
                            : 'border-b border-[#9850b2] pb-8 px-2'
                    }`}
                >
                    {state === 'collapsed' ? (
                        <img
                            src={w}
                            alt="VAIS Academy"
                            width={211}
                            height={211}
                            className="h-9 w-9 object-contain"
                        />
                    ) : (
                        <img
                            src={theme === 'light' ? schoolLogoWhite1 : schoolLogo}
                            alt="VAIS Academy"
                            width={1024}
                            height={212}
                            className="h-10 w-auto max-w-full object-contain"
                        />
                    )}
                </div>
                {/* for version  */}
                <div className=" flex  text-md leading-tight ml-10 justify-between mt-4">
                    <span className={`${state === 'collapsed' ? 'hidden' : 'block '} ${theme=== 'light' ? 'text-[#b39e29]' : "text-[#6b289e]"} `}>School</span> <span className={`${state === 'collapsed' ? 'hidden' : 'block'} ${theme=== 'light' ? 'text-[#b39e29]' : "text-[#6b289e]"}  `}>V-{version}</span>
                </div>
            </SidebarMenuItem>
        </SidebarMenu>
    )
}

export default TeamSwitcher